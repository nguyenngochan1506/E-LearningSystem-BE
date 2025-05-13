package vn.edu.hcmuaf.fit.elearning.feature.users;

import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.AssignRoleToUserRequest;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.req.UserChangePasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.req.UserCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.req.UserUpdateInfoRequest;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.res.UserPageResponse;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.res.UserResponse;

public interface UserService {
    long createUser(UserCreationRequest req);

    UserResponse getUserById(Long id);

    long updateUserInfoById(UserUpdateInfoRequest userUpdateRequest);

    long changePassword(UserChangePasswordRequest userChangePasswordRequest);

    UserPageResponse getAllUsers(String keyword, String sort, int pageNo, int pageSize, boolean isDeleted);

    long deleteUserById(Long id);

    long restoreUserById(Long id);

    UserResponse getMe();

    long assignRoleToUser( AssignRoleToUserRequest req);
    UserEntity findByEmail(String email);
}
