package vn.edu.hcmuaf.fit.elearning.feature.auth.service;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.ForgotPasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.LoginRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.ResetPasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.SignupRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.AuthResponse;

import java.text.ParseException;

public interface AuthService {
    AuthResponse authenticate(LoginRequest loginRequest) throws JOSEException;

    long signup(SignupRequest signupRequest);

    AuthResponse refreshToken(HttpServletRequest request) throws ParseException, JOSEException;

    long forgotPassword(ForgotPasswordRequest req) throws JOSEException;

    long resetPassword(ResetPasswordRequest request) throws ParseException, JOSEException;

    String removeToken(HttpServletRequest request) throws ParseException;
}
