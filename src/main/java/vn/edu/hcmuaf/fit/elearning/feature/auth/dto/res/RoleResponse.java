package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class RoleResponse implements Serializable {
    private final long id;
    private final String name;
    private final String description;
    private final List<PermissionResponse> permissions;
}
