package vn.edu.hcmuaf.fit.elearning.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalHandleException {
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

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleDuplicateKey(SQLException ex, WebRequest request) {
        String message = ex.getMessage();
        String customMessage = message.substring(message.indexOf("("), message.lastIndexOf(")"))
                .replace("(", "")
                .replace(")", "") +
                " "+ Translator.translate("error.duplicate");

        return ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.BAD_REQUEST.value())
                .message(customMessage)
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .path(request.getDescription(false).replace("uri=", ""))
                .build();
    }
}
