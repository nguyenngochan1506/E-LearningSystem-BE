package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResetPasswordRequest implements Serializable {
    @NotBlank(message = "Email is required")
    private String secretKey;
    @NotBlank(message = "NewPassword is required")
    private String newPassword;
}
