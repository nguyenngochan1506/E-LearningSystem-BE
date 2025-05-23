package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.elearning.common.enums.HttpMethod;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class PermissionCreationRequest implements Serializable {
    @NotNull(message = "error.validate.not-blank")
    private HttpMethod method;

    @NotBlank(message = "error.validate.not-blank")
    private String path;

    @NotBlank(message = "error.validate.not-blank")
    private String description;

    @NotBlank(message = "error.validate.not-blank")
    private String module;
}
