package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

@Getter
public class AssignRoleToUserRequest implements Serializable {
    @NotNull(message = "error.validate.not-blank")
    private Long userId;
    @NotNull(message = "error.validate.not-blank")
    private Set<Long> roleIds;
}
