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
import vn.edu.hcmuaf.fit.elearning.feature.auth.repository.RedisTokenRepository;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.AuthService;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.JwtService;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.LoginRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.SignupRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.AuthResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.RedisTokenService;
import vn.edu.hcmuaf.fit.elearning.feature.user.UserEntity;
import vn.edu.hcmuaf.fit.elearning.feature.user.UserRepository;

import java.text.ParseException;


@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenService redisTokenService;
    private final JwtService jwtService;

    @Override
    public AuthResponse authenticate(LoginRequest loginRequest) throws JOSEException {
        UserEntity user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new BadCredentialsException(Translator.translate("auth.login.error")));

        // Check if the user
        if (user.isDeleted() || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException(Translator.translate("auth.login.error"));
        }
        // Check if the user is locked or inactive
        if (user.getStatus() == UserStatus.INACTIVE) {
            throw new BadCredentialsException(Translator.translate("auth.login.error.inactive"));
        }
        if (user.getStatus() == UserStatus.BLOCKED) {
            throw new BadCredentialsException(Translator.translate("auth.login.error.locked"));
        }

        // generate token
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);
        // save token to redis
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

    @Override
    public long signup(SignupRequest signupRequest) {
        log.info("Signing up {}", signupRequest.getEmail());

        UserEntity user = new UserEntity();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFullName(signupRequest.getFullName());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setGender(signupRequest.getGender());
        user.setDateOfBirth(signupRequest.getDateOfBirth());
        user.setStatus(UserStatus.INACTIVE);

        userRepository.save(user);
        log.info("User {} signed up successfully", signupRequest.getEmail());

        return user.getId();
    }

    @Override
    public AuthResponse refreshToken(HttpServletRequest request) throws ParseException, JOSEException {
        //get token from request header
        String token = request.getHeader("Referer");

        //validate token
        if (!StringUtils.hasLength(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }
        if(!jwtService.validateToken(token, TokenType.REFRESH_TOKEN)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        //get user from token
        String email = jwtService.getEmailFromToken(token);

        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("user.not-found")));

        //generate new token
        String accessToken = jwtService.generateToken(user, TokenType.ACCESS_TOKEN);
        String refreshToken = jwtService.generateToken(user, TokenType.REFRESH_TOKEN);

        //check token in redis
        RedisTokenEntity storedToken = redisTokenService.getToken(user.getEmail());
        if (storedToken == null || !storedToken.getRefreshToken().equals(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        //save token to redis
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

    @Override
    public long forgotPassword(ForgotPasswordRequest req) throws JOSEException {
        //get user by email
        UserEntity user = userRepository.findByEmail(req.getEmail()).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("user.not-found")));
        //generate token
        String token = jwtService.generateToken(user, TokenType.RESET_TOKEN);

        //save token to redis
        RedisTokenEntity redisToken = RedisTokenEntity.builder()
                .id(user.getEmail())
                .resetToken(token)
                .build();
        redisTokenService.saveToken(redisToken);
        //send email
        String urlResetPassword = "http://localhost:8080/api/auth/reset-password?secretKey=" + token;

        System.out.println("Reset password link: " + urlResetPassword);

        return user.getId();
    }

    @Override
    public long resetPassword(ResetPasswordRequest request) throws ParseException, JOSEException {
        // check token
        String token = request.getSecretKey();
        if (!StringUtils.hasLength(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }
        if(!jwtService.validateToken(token, TokenType.RESET_TOKEN)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        //get user from token
        String email = jwtService.getEmailFromToken(token);

        //check token in redis
        RedisTokenEntity storedToken = redisTokenService.getToken(email);
        if (storedToken == null || !storedToken.getResetToken().equals(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        log.info("Resetting password for user {}", email);
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("user.not-found")));

        //update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("User {} reset password successfully", user.getEmail());
        redisTokenService.deleteToken(email);
        return user.getId();
    }

    @Override
    public String removeToken(HttpServletRequest request) throws ParseException {
        final String token = request.getHeader("Referer");
        if (!StringUtils.hasLength(token)) {
            throw new BadCredentialsException(Translator.translate("auth.token.invalid"));
        }

        String email = jwtService.getEmailFromToken(token);

        // delete token from redis
        redisTokenService.deleteToken(email);
        log.info("User {} logged out successfully", email);
        return Translator.translate("auth.logout.success");
    }
}
