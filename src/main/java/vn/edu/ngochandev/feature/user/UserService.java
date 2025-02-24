package vn.edu.ngochandev.feature.user;


import vn.edu.ngochandev.feature.user.dto.request.UserChangePasswordRequest;
import vn.edu.ngochandev.feature.user.dto.request.UserCreationRequest;
import vn.edu.ngochandev.feature.user.dto.request.UserUpdateRequest;
import vn.edu.ngochandev.feature.user.dto.response.UserResponse;

import java.util.List;

public interface UserService {

    List<UserResponse> findAll();
    UserResponse findById(Long id);
    UserResponse findByEmail(String email);
    UserResponse findByUsername(String username);
    Long saveUser(UserCreationRequest req);
    void updateUser(UserUpdateRequest req);
    void deleteUser(Long id);
    void changePassword(UserChangePasswordRequest req);

}
