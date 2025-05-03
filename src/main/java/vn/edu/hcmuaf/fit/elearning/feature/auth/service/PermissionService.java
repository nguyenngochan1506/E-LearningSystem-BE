package vn.edu.hcmuaf.fit.elearning.feature.auth.service;

import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.PermissionCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.PermissionUpdateRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.PermissionPageResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.PermissionResponse;

public interface PermissionService {
    long createPermission(PermissionCreationRequest request);
    long updatePermission( PermissionUpdateRequest request);
    long deletePermission(long id);
    PermissionResponse getPermissionById(long id);
    PermissionPageResponse getAllPermissions(int pageNo, int pageSize, boolean isDeleted);
    long restorePermission(long id);
}
