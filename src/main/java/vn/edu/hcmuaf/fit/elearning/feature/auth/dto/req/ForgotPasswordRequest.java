package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ForgotPasswordRequest implements Serializable {
    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;
}
