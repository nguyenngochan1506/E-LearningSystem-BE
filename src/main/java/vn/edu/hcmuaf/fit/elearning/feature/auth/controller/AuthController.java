package vn.edu.hcmuaf.fit.elearning.feature.auth.controller;

import com.nimbusds.jose.JOSEException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ErrorResponse;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.ForgotPasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.LoginRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.ResetPasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.SignupRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.AuthService;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserService;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.res.UserResponse;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@Tag(name = "AUTH", description = "Auth API")
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @Operation(
            summary = "Reset password",
            description = "Reset password for the user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully reset password",
                    content = {@Content(
                            schema = @Schema(implementation = ResponseDto.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                    examples = @ExampleObject(
                            name = "Reset Password Success",
                            description = "Reset Password Success",
                            value = """
                                    {
                                      "status": 200,
                                      "message": "Reset password successfully",
                                      "data": 1
                                    }
                                    """
                    ))})
    })
    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto resetPassword(@Valid @RequestBody ResetPasswordRequest request) throws JOSEException, ParseException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.reset-password.success"))
                .data(authService.resetPassword(request))
                .build();
    }

    @Operation(
            summary = "Forgot password",
            description = "Send a reset password email to the user")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully sent reset password email",
                    content = {@Content(
                            schema = @Schema(implementation = ResponseDto.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Reset Password Success",
                                    description = "Reset Password Success",
                                    value = """
                                    {
                                      "status": 200,
                                      "message": "Reset password successfully",
                                      "data": 1
                                    }
                                    """
                            ))})
    })
    @PostMapping("/forgot-password")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) throws JOSEException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.forgot-password.success"))
                .data(authService.forgotPassword(request))
                .build();
    }

    @Operation(
            summary = "Retrieve my profile",
            description = "Get the current user's profile information")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile", content = {@Content(schema = @Schema(implementation = UserResponse.class), mediaType = MediaType.APPLICATION_JSON_VALUE)}),
    })
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getMe() {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.me.success"))
                .data(userService.getMe())
                .build();
    }
    @Operation(
            summary = "Refresh token",
            description = "Refresh the access token using the refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successfully refreshed token",
                    content = {@Content(
                            schema = @Schema(implementation = ResponseDto.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Refresh Token Success",
                                    description = "Refresh Token Success",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Refresh token successfully",
                                              "data": {
                                                "accessToken": "...",
                                                "refreshToken": "...",
                                                "userId": 1
                                              }
                                            }
                                            """
                            ))})
    })
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto refreshToken(HttpServletRequest request) throws ParseException, JOSEException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.refresh.success"))
                .data(authService.refreshToken(request))
                .build();
    }

    @Operation(
            summary = "Login",
            description = "Authenticate user and return access token")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successfully logged in",
                    content = {@Content(
                            schema = @Schema(implementation = ResponseDto.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Login Success",
                                    description = "Login Success",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Login successfully",
                                              "data": {
                                                "accessToken": "...",
                                                "refreshToken": "...",
                                                "userId": 1
                                              }
                                            }
                                            """
                            ))})
    })
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto login(@Valid @RequestBody LoginRequest loginRequest) throws JOSEException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.login.success"))
                .data(authService.authenticate(loginRequest))
                .build();
    }


    @Operation(
            summary = "Logout",
            description = "Logout the user and invalidate the refresh token")
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "Successfully logged out",
                    content = {@Content(
                            schema = @Schema(implementation = ResponseDto.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Logout Success",
                                    description = "Logout Success",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Logout successfully",
                                              "data": "Logout successfully"
                                            }
                                            """
                            ))})
    })
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto logout(HttpServletRequest request) throws ParseException {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("auth.logout.success"))
                .data(authService.removeToken(request))
                .build();
    }

    @Operation(
            summary = "Sign up",
            description = "Create a new user account")
    @ApiResponses({
            @ApiResponse(responseCode = "201",
                    description = "Successfully signed up",
                    content = {@Content(
                            schema = @Schema(implementation = ResponseDto.class),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "Sign Up Success",
                                    description = "Sign Up Success",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Sign Up successfully",
                                              "data": 1
                                            }
                                            """
                            ))}),
            @ApiResponse(responseCode = "409",
                    description = "handle duplicate key",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "409 Conflict",
                                    summary = "Handle Duplicate Key",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 409,
                                              "path": "/api/v1/...",
                                              "error": "Conflict",
                                              "message": "email=123@gmail.com is already exists"
                                            }
                                            """
                            ))})

            })
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
