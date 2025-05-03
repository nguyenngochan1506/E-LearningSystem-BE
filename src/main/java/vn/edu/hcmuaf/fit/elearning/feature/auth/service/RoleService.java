package vn.edu.hcmuaf.fit.elearning.feature.auth.service;

import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.RoleCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.RoleUpdateRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.RolePageResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.RoleResponse;

public interface RoleService {
    long createRole(RoleCreationRequest request);
    long updateRole(RoleUpdateRequest request);
    long deleteRole(long id);
    RoleResponse getRoleById(long id);
    RolePageResponse getAllRoles(int pageNo, int pageSize, boolean isDeleted);

    long restoreRole(long id);
}
