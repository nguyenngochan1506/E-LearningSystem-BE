package vn.edu.ngochandev.feature.auth;

import vn.edu.ngochandev.feature.auth.dto.SignInRequest;
import vn.edu.ngochandev.feature.auth.dto.TokenResponse;

public interface AuthenticationService {
    public TokenResponse authenticate(SignInRequest signInRequest);
}
