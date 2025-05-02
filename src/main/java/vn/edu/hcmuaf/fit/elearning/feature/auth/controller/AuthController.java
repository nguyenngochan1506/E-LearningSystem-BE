package vn.edu.hcmuaf.fit.elearning.feature.auth.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.ForgotPasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.LoginRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.ResetPasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.SignupRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.AuthService;
import vn.edu.hcmuaf.fit.elearning.feature.user.UserService;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto resetPassword(@Valid @RequestBody ResetPasswordRequest request) throws JOSEException, ParseException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.reset-password.success"))
                .data(authService.resetPassword(request))
                .build();
    }

    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) throws JOSEException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.forgot-password.success"))
                .data(authService.forgotPassword(request))
                .build();
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getMe() {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.me.success"))
                .data(userService.getMe())
                .build();
    }
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto refreshToken(HttpServletRequest request) throws ParseException, JOSEException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.refresh.success"))
                .data(authService.refreshToken(request))
                .build();
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto login(@Valid @RequestBody LoginRequest loginRequest) throws JOSEException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.login.success"))
                .data(authService.authenticate(loginRequest))
                .build();
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto logout(HttpServletRequest request) throws ParseException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.logout.success"))
                .data(authService.removeToken(request))
                .build();
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto signup(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("auth.signup.success"))
                .data(authService.signup(signupRequest))
                .build();
    }
}
