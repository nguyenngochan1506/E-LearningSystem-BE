package vn.edu.hcmuaf.fit.elearning.feature.auth.service;

import com.nimbusds.jose.JOSEException;
import vn.edu.hcmuaf.fit.elearning.common.enums.TokenType;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserEntity;

import java.text.ParseException;

public interface JwtService {
    String generateToken(UserEntity user, TokenType type) throws JOSEException;
    boolean validateToken(String token, TokenType type) throws JOSEException, ParseException;

    String getEmailFromToken(String token) throws ParseException;
}
