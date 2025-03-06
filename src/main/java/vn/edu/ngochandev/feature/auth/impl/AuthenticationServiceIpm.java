package vn.edu.ngochandev.feature.auth.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import vn.edu.ngochandev.exception.ResourceNotFoundException;
import vn.edu.ngochandev.feature.auth.AuthenticationService;
import vn.edu.ngochandev.feature.auth.JwtService;
import vn.edu.ngochandev.feature.auth.dto.SignInRequest;
import vn.edu.ngochandev.feature.auth.dto.TokenResponse;
import vn.edu.ngochandev.feature.user.UserEntity;
import vn.edu.ngochandev.feature.user.UserRepository;

@Service
@Slf4j(topic = "AUTHENTICATION-SERVICE")
@RequiredArgsConstructor
public class AuthenticationServiceIpm implements AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @Override
    public TokenResponse authenticate(SignInRequest signInRequest) {
        log.info("Authenticating {},{}", signInRequest.getUsername(), signInRequest.getPlatform());
        authManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));
        UserEntity user =  userRepository.findByUsername(signInRequest.getUsername()).orElseThrow(() -> new ResourceNotFoundException("Username or password is incorrect"));

        String accessToken = jwtService.generateToken(user);
        String refreshToken = "DUMMY-REFRESH-TOKEN";

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }
}
