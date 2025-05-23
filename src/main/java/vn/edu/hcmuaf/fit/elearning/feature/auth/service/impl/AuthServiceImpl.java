package vn.edu.hcmuaf.fit.elearning.feature.auth.service.impl;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.edu.hcmuaf.fit.elearning.common.enums.TokenType;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.common.enums.UserStatus;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.ForgotPasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.ResetPasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RedisTokenEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.AuthService;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.JwtService;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.LoginRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.SignupRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.AuthResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.RedisTokenService;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserEntity;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserRepository;

import java.text.ParseException;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenService redisTokenService;
    private final JwtService jwtService;
    /**
     * Authenticates a user based on their email and password, returning access and refresh tokens.
     *
     * @param loginRequest The login request containing the user's email and password.
     * @return An {@link AuthResponse} containing the access token, refresh token, and user ID.
     * @throws JOSEException If there is an error generating JWT tokens.
     * @throws BadCredentialsException If the credentials are invalid, the user is inactive, blocked, or deleted.
     */
    @Override
    public AuthResponse authenticate(LoginRequest loginRequest) throws JOSEException {
        // Retrieve user by email or throw an exception if not found
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new BadCredentialsException(Translator.translate("auth.login.error")));

        // Validate user credentials and account status
        if (user.isDeleted() || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(Translator.translate("auth.login.error"));
        }
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new BadCredentialsException(Translator.translate("auth.login.error.inactive"));
        }
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new BadCredentialsException(Translator.translate("auth.login.error.locked"));
        }

        //  Generate JWT access and refresh tokens
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        // Store tokens in Redis for session management
        RedisTokenEntity redisToken = RedisTokenEntity.builder()
                .id(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        redisTokenService.saveToken(redisToken);
        log.info("User {} logged in successfully", user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    /**
     * Registers a new user with the provided details and sets their initial status to inactive.
     *
     * @param signupRequest The signup request containing user details (email, password, full name, etc.).
     * @return The ID of the newly created user.
     * @throws DataIntegrityViolationException If the email already exists or other database constraints are violated.
     */
    @Override
    public long signup(SignupRequest signupRequest) {
        log.info("Signing up {}", signupRequest.getEmail());

        // Create new user entity with provided details
        UserEntity user = new UserEntity();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setGender(signupRequest.getGender());
        user.setDateOfBirth(signupRequest.getDateOfBirth());
        user.setStatus(UserStatus.INACTIVE);// Set to inactive pending activation

        userRepository.save(user);
        log.info("User {} signed up successfully", signupRequest.getEmail());

        return user.getId();
    }

    /**
     * Refreshes the access and refresh tokens using a valid refresh token from the request header.
     *
     * @param request The HTTP request containing the refresh token in the "Referer" header.
     * @return An {@link AuthResponse} containing new access token, refresh token, and user ID.
     * @throws ParseException If the token cannot be parsed.
     * @throws JOSEException If there is an error generating new JWT tokens.
     * @throws BadCredentialsException If the token is invalid or does not match the stored token.
     * @throws ResourceNotFoundException If the user associated with the token is not found.
     */
    @Override
    public AuthResponse refreshToken(HttpServletRequest request) throws ParseException, JOSEException {
        //Extract refresh token from Referer header (non-standard, used for specific client compatibility)
        String token = request.getHeader("Referer");

        //validate token
        if (!StringUtils.hasLength(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }
        if(!jwtService.validateToken(token, TokenType.REFRESH_TOKEN)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        //Retrieve user associated with the token
        String email = jwtService.getEmailFromToken(token);

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("user.not-found")));

        //Generate new access and refresh tokens
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);

        //Verify token against Redis store
        RedisTokenEntity storedToken = redisTokenService.getToken(user.getEmail());
        if (storedToken == null || !storedToken.getRefreshToken().equals(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        //Store new tokens in Redis for session management
        RedisTokenEntity redisToken = RedisTokenEntity.builder()
                .id(user.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        redisTokenService.saveToken(redisToken);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    /**
     * Initiates the password reset process by generating a reset token and storing it in Redis.
     *
     * @param req The request containing the user's email.
     * @return The ID of the user requesting the password reset.
     * @throws JOSEException If there is an error generating the reset token.
     * @throws ResourceNotFoundException If the user with the provided email is not found.
     */
    @Override
    public long forgotPassword(ForgotPasswordRequest req) throws JOSEException {
        //Retrieve user by email
        UserEntity user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("user.not-found")));
        // Generate password reset token
        String token = jwtService.generateToken(user, TokenType.RESET_TOKEN);

        //save token to redis
        RedisTokenEntity redisToken = RedisTokenEntity.builder()
                .id(user.getEmail())
                .resetToken(token)
                .build();
        redisTokenService.saveToken(redisToken);
        // TODO: Replace with actual email service to send reset link
        String urlResetPassword = "http://localhost:8080/api/auth/reset-password?secretKey=" + token;

        System.out.println("Reset password link: " + urlResetPassword);

        return user.getId();
    }
    /**
     * Resets the user's password using a valid reset token.
     *
     * @param request The request containing the reset token and new password.
     * @return The ID of the user whose password was reset.
     * @throws ParseException If the token cannot be parsed.
     * @throws JOSEException If there is an error processing the token.
     * @throws BadCredentialsException If the token is invalid or does not match the stored token.
     * @throws ResourceNotFoundException If the user associated with the token is not found.
     */
    @Override
    public long resetPassword(ResetPasswordRequest request) throws ParseException, JOSEException {
        // Validate reset token presence and type
        String token = request.getSecretKey();
        if (!StringUtils.hasLength(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }
        if(!jwtService.validateToken(token, TokenType.RESET_TOKEN)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        //Retrieve user associated with the token
        String email = jwtService.getEmailFromToken(token);

        //Verify token against Redis store
        RedisTokenEntity storedToken = redisTokenService.getToken(email);
        if (storedToken == null || !storedToken.getResetToken().equals(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        log.info("Resetting password for user {}", email);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("user.not-found")));

        //Update password and log the event
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("User {} reset password successfully", user.getEmail());

        // Remove reset token from Redis to prevent reuse
        redisTokenService.deleteToken(email);
        return user.getId();
    }
    /**
     * Logs out a user by removing their tokens from Redis.
     *
     * @param request The HTTP request containing the token in the "Referer" header.
     * @return A success message indicating logout completion.
     * @throws ParseException If the token cannot be parsed.
     * @throws BadCredentialsException If the token is invalid or missing.
     */
    @Override
    public String removeToken(HttpServletRequest request) throws ParseException {
        // Extract token from Referer header (non-standard, used for specific client compatibility)
        final String token = request.getHeader("Referer");
        if (!StringUtils.hasLength(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }
        // Retrieve user email from token
        String email = jwtService.getEmailFromToken(token);

        // Remove tokens from Redis to invalidate session
        redisTokenService.deleteToken(email);
        log.info("User {} logged out successfully", email);
        return Translator.translate("auth.logout.success");
    }
}
