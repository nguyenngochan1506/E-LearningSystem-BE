package vn.edu.hcmuaf.fit.elearning.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import vn.edu.hcmuaf.fit.elearning.common.ErrorResponse;
import vn.edu.hcmuaf.fit.elearning.common.Translator;

import java.io.IOException;
import java.util.Date;

public class JwtAuthEntryPoint  implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(new Date(System.currentTimeMillis()))
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(request.getRequestURI())
                .message(Translator.translate("auth.unauthorized"))
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
        response.flushBuffer();
    }
}
