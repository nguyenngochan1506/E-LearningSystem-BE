package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.BaseResponse;

import java.util.List;

@Getter
@Setter
@SuperBuilder
public class RoleResponse extends BaseResponse {
    private final long id;
    private final String name;
    private final String description;
    private final List<PermissionResponse> permissions;
}
