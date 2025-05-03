package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vn.edu.hcmuaf.fit.elearning.common.enums.HttpMethod;

import java.io.Serializable;

@Getter
public class PermissionUpdateRequest implements Serializable {
    @NotNull(message = "error.validate.not-blank")
    private Long id;

    @NotNull(message = "error.validate.not-blank")
    private HttpMethod method;

    @NotBlank(message = "error.validate.not-blank")
    private String path;

    @NotBlank(message = "error.validate.not-blank")
    private String description;

    @NotBlank(message = "error.validate.not-blank")
    private String module;
}
