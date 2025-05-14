package vn.edu.hcmuaf.fit.elearning.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import vn.edu.hcmuaf.fit.elearning.common.ErrorResponse;
import vn.edu.hcmuaf.fit.elearning.common.Translator;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;


@Slf4j
@RestControllerAdvice
public class GlobalHandleException {
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request - Invalid input parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "400 Bad Request",
                                    summary = "Invalid input parameters",
                                    value = """
                {
                  "timestamp": "2025-05-14T09:09:00.000+07:00",
                  "status": 400,
                  "path": "/api/v1/users",
                  "error": "argument invalid",
                  "message": "username: must not be blank, email: must be a valid email address"
                }
                """
                            )
                    )
            )
    })
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(s -> {
                    String fieldName = s.getField();
                    String message = Translator.translate(s.getDefaultMessage());
                    return fieldName + " " + message;
                })
                .toList();
        return ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(errors.toString().substring(1, errors.toString().length() - 1))
                .path(request.getDescription(false).replace("uri=", ""))
                .error(Translator.translate("error.argument.invalid"))
                .build();
    }
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "400 Bad Request",
                                    summary = "Handle Bad Request",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 400,
                                              "path": "/api/v1/...",
                                              "error": "Argument is invalid",
                                              "message": "{data} is invalid",
                                            }
                                            """
                            ))})
    })
    @ExceptionHandler({IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleLegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .error(Translator.translate("error.argument.invalid"))
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "404 Response",
                                    summary = "Handle exception when resource not found",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 404,
                                              "path": "/api/v1/...",
                                              "error": "Not Found",
                                              "message": "{data} not found"
                                            }
                                            """
                            ))})
    })
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getDescription(false).replace("uri=", ""))
                .error(Translator.translate("error.argument.invalid"))
                .build();
    }
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "404 Response",
                                    summary = "Enum not valid",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 400,
                                              "path": "/api/v1/...",
                                              "error": "argument invalid",
                                              "message": "Gender must be [MALE, FEMALE, OTHER]"
                                            }
                                            """
                            ))})
    })
    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationEnumException(HttpMessageNotReadableException ex, WebRequest request) {
        String message = ex.getMessage();
        String customMessage = "";

        customMessage = message.substring(message.indexOf("common.enums.") + "common.enums.".length(), message.indexOf("from")-2)
                + " must be " + message.substring(message.indexOf("["), message.lastIndexOf("]") +1);
        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.BAD_REQUEST.value())
                .message(customMessage)
                .error(Translator.translate("error.argument.invalid"))
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "401 Unauthorized",
                                    summary = "Handle Unauthorized",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 401,
                                              "path": "/api/v1/...",
                                              "error": "Unauthorized",
                                              "message": "unauthorized"
                                            }
                                            """
                            ))})
    })
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentialException(BadCredentialsException ex, WebRequest request) {

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }
    @ApiResponses(value = {
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
    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateKey(SQLException ex, WebRequest request) {
        String message = ex.getMessage();
        String customMessage = message.substring(message.indexOf("("), message.lastIndexOf(")"))
                .replace("(", "")
                .replace(")", "") +
                " "+ Translator.translate("error.duplicate");

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.CONFLICT.value())
                .message(customMessage)
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500",
                    description = "Internal Server Error",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "500 Internal Server Error",
                                    summary = "Handle Internal Server Error",
                                    value = """
                                            {
                                              "timestamp": "2023-10-19T06:07:35.321+00:00",
                                              "status": 500,
                                              "path": "/api/v1/...",
                                              "error":"Internal Server Error",
                                              "message":"Something went wrong"
                                            }
                                            """
                            ))})
    })
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllException(Exception ex, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden - Access denied",
                    content = {@Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = @ExampleObject(
                                    name = "403 Forbidden",
                                    summary = "Access denied due to insufficient permissions",
                                    value = """
                    {
                      "timestamp": "2025-05-14T09:59:00.000+07:00",
                      "status": 403,
                      "path": "/api/v1/...",
                      "error": "Forbidden",
                      "message": "Access denied: You do not have permission to perform this action"
                    }
                    """
                            )
                    )}
            )
    })
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        return ErrorResponse.builder()
                .timestamp(new Date())
                .status(HttpStatus.FORBIDDEN.value())
                .message(Translator.translate("error.access.denied"))
                .error(HttpStatus.FORBIDDEN.getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }

}
