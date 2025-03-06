package vn.edu.ngochandev.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleGlobalException(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        String message = ex.getMessage();
        int start = message.indexOf(":") +1;
        int end = message.indexOf("\r");
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class, HandlerMethodValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        String message = ex.getMessage();
        if (ex instanceof ConstraintViolationException || ex instanceof HandlerMethodValidationException g) {
            response.setError("PathVariable invalid");

        }else if (ex instanceof MethodArgumentNotValidException) {
            response.setError("Payload invalid");
            int start = message.lastIndexOf("[") + 1;
            int end = message.lastIndexOf("]") - 1;
            message = message.substring(start, end);
        }
        response.setMessage(message);
        return response;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleInternalServerException(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setPath(request.getDescription(false).replace("uri=", ""));
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        String message = ex.getMessage();
        if( ex instanceof MethodArgumentTypeMismatchException){
            response.setError("Parameter type mismatch");
        }
        response.setMessage(message);
        return response;
    }



    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "bad request",
            content = {
                    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            examples = {@ExampleObject(
                                    name = "404 Response",
                                    summary = "Handle exception when resource not found",
                                    value = """
                                            {
                                            "timestamp": "2025-03-04T12:38:39.2568996"
                                            "status": 404
                                            "path": /api/v1/...
                                            "error": "Not Found"
                                            "message": "{data} not found"
                                            }
                                            """
                            )})})})
    public ErrorResponse handleResourceNotFound(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setMessage(ex.getMessage());
        response.setError(HttpStatus.NOT_FOUND.getReasonPhrase());
        response.setPath(request.getDescription(false).replace("uri=", ""));
    return response;
    }


    @ExceptionHandler({ DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "409", description = "conflict",
                    content = {
                            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {@ExampleObject(
                                            name = "409 Response",
                                            summary = "Handle exception when duplicate value",
                                            value = """
                                            {
                                            "timestamp": "2025-03-04T12:38:39.2568996" 
                                            "status": 400 
                                            "path": /api/v1/... 
                                            "error": "Duplicate" 
                                            "message": "{data} already in database" 
                                            }
                                            """
                                    )})})})
    public ErrorResponse handleDuplicateValue(Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse();
        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.CONFLICT.value());
        response.setError("Duplicate Key");
        response.setPath(request.getDescription(false).replace("uri=", ""));

        String message = ex.getMessage();
        int start = message.indexOf("Detail: Key") + 11;
        int end = message.lastIndexOf("exists")+"exists".length();
        response.setMessage(message.substring(start, end));
        return response;
    }
}
