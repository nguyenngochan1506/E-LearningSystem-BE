package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import vn.edu.hcmuaf.fit.elearning.common.enums.Platform;

import java.io.Serializable;

@Getter
public class LoginRequest implements Serializable {
    @NotBlank(message = "error.validate.not-blank")
    private String email;
    @NotBlank(message = "error.validate.not-blank")
    private String password;
    @NotNull(message = "error.validate.not-blank")
    private Platform platform;
    @NotBlank(message = "error.validate.not-blank")
    private String deviceToken;
}
