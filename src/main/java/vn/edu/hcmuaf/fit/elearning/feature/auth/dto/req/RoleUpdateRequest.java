package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class RoleUpdateRequest implements Serializable {
    @NotNull(message = "error.validate.not-blank")
    private Long id;

    @NotBlank(message = "error.validate.not-blank")
    private String name;

    @NotBlank(message = "error.validate.not-blank")
    private String description;
}
