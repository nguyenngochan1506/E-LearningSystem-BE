package vn.edu.hcmuaf.fit.elearning.feature.auth.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.enums.TokenType;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RoleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.JwtService;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserEntity;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secretAccessTokenKey}")
    private String secretAccessTokenKey;
    @Value("${jwt.secretRefreshTokenKey}")
    private String secretRefreshTokenKey;
    @Value("${jwt.secretResetTokenKey}")
    private String secretResetTokenKey;
    @Value("${jwt.accessTokenExpirationTime}")
    private Long accessTokenExpirationTime;
    @Value("${jwt.refreshTokenExpirationTime}")
    private Long refreshTokenExpirationTime;
    @Value("${jwt.resetTokenExpirationTime}")
    private Long resetTokenExpirationTime;
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * Generates a JWT token for a user with the specified token type (access, refresh, or reset).
     *
     * @param user The user entity for whom the token is generated.
     * @param type The type of token to generate (ACCESS_TOKEN, REFRESH_TOKEN, or RESET_TOKEN).
     * @return A signed JWT token as a string.
     * @throws JOSEException If there is an error during token signing or creation.
     */
    @Override
    public String generateToken(UserEntity user, TokenType type) throws JOSEException {
        log.info("Generating JWT for user: {}", user.getEmail());

        // Configure JWT header with HS256 algorithm for secure signing
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // Set expiration time based on token type
        Date expirationTime;
        if (type == TokenType.ACCESS_TOKEN){
            expirationTime = new Date(System.currentTimeMillis() + accessTokenExpirationTime * 1000 * 60);
        } else if (type == TokenType.REFRESH_TOKEN) {
            expirationTime = new Date(System.currentTimeMillis() + refreshTokenExpirationTime * 1000 * 60);
        } else{
            expirationTime = new Date(System.currentTimeMillis() + resetTokenExpirationTime * 1000 * 60);
        }
        //Build JWT claims with user details and roles
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issuer(issuer)
                .issueTime(new Date())
                .expirationTime(expirationTime)
                .claim("roles", buildRoles(user))
                .build();

        // create payload
        Payload payload = new Payload(claimsSet.toJSONObject());

        // create JWT
        JWSObject jwsObject = new JWSObject(header, payload);

        // Create and sign JWT with appropriate secret key
        String secretKey = type == TokenType.ACCESS_TOKEN ? secretAccessTokenKey : type == TokenType.REFRESH_TOKEN ? secretRefreshTokenKey : secretResetTokenKey;
        jwsObject.sign(new MACSigner(secretKey));
        // serialize the JWT to a string
        return jwsObject.serialize();
    }
    /**
     * Validates a JWT token by checking its signature and expiration.
     *
     * @param token The JWT token to validate.
     * @param type  The expected type of token (ACCESS_TOKEN, REFRESH_TOKEN, or RESET_TOKEN).
     * @return True if the token is valid and not expired, false otherwise.
     * @throws JOSEException   If there is an error verifying the token signature.
     * @throws ParseException If the token cannot be parsed.
     */
    @Override
    public boolean validateToken(String token, TokenType type) throws JOSEException, ParseException {
        byte[] secretKeyBytes;
        if (type == TokenType.ACCESS_TOKEN) {
            secretKeyBytes = secretAccessTokenKey.getBytes();
        } else if (type == TokenType.REFRESH_TOKEN) {
            secretKeyBytes = secretRefreshTokenKey.getBytes();
        } else{
            secretKeyBytes = secretResetTokenKey.getBytes();
        }
        JWSVerifier verifier = new MACVerifier(secretKeyBytes);
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        // Check if the token is expired
        return expirationTime != null && expirationTime.after(new Date()) &&
                signedJWT.verify(verifier);
    }

    /**
     * Extracts the email (subject) from a JWT token.
     * 
     * @param token The JWT token.
     * @return The email (subject) extracted from the token.
     * @throws ParseException If the token cannot be parsed.
     */
    @Override
    public String getEmailFromToken(String token) throws ParseException {
        return SignedJWT.parse(token).getJWTClaimsSet().getSubject();
    }

    /**
     * Builds a list of role names from the user's roles.
     * 
     * @param user The user entity.
     * @return A list of role names.
     */
    private List<String> buildRoles(UserEntity user) {
        // Map each RoleEntity to its name and collect into a list
        return user.getRoles().stream().map(RoleEntity::getName).toList();
    }
}
