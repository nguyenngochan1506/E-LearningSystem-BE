package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class AssignRoleToPermissionRequest implements Serializable {
    @NotNull(message = "error.validate.not-blank")
    private Long roleId;

    @NotNull(message = "error.validate.not-blank")
    private Long[] permissionIds;
}
