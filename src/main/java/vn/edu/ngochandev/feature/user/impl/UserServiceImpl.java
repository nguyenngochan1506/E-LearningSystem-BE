package vn.edu.ngochandev.feature.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.ngochandev.common.UserStatus;
import vn.edu.ngochandev.feature.user.dto.request.UserChangePasswordRequest;
import vn.edu.ngochandev.feature.user.dto.request.UserCreationRequest;
import vn.edu.ngochandev.feature.user.dto.request.UserUpdateRequest;
import vn.edu.ngochandev.feature.user.dto.response.UserResponse;
import vn.edu.ngochandev.feature.user.UserEntity;
import vn.edu.ngochandev.feature.user.UserRepository;
import vn.edu.ngochandev.feature.user.UserService;

import java.util.List;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> findAll() {
        return List.of();
    }

    @Override
    public UserResponse findById(Long id) {
        return null;
    }

    @Override
    public UserResponse findByEmail(String email) {
        return null;
    }

    @Override
    public UserResponse findByUsername(String username) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveUser(UserCreationRequest req) {
        log.info("Saving user {}", req);
        UserEntity user = new UserEntity();

        user.setFullName(req.getFullName());
        user.setGender(req.getGender());
        user.setDateOfBirth(req.getDateOfBirth());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setEmail(req.getEmail());
        user.setUserName(extractStudentIdFromEmail(req.getEmail()));
        user.setPassword(req.getPassword());
        user.setStatus(UserStatus.ACTIVE);
        user.setUserType(req.getUserType());

        userRepository.save(user);

        log.info("Saved user {}", user);
        return user.getId();
    }

    @Override
    public void updateUser(UserUpdateRequest req) {

    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public void changePassword(UserChangePasswordRequest req) {

    }

    private String extractStudentIdFromEmail(String email) {
        String[] parts = email.split("@");
        return parts[0];
    }
}

