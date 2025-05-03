package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.PageResponse;

import java.util.List;

@Getter
@SuperBuilder
public class RolePageResponse extends PageResponse {
    private List<RoleResponse> roles;
}
