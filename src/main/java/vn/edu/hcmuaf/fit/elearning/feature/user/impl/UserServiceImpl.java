package vn.edu.hcmuaf.fit.elearning.feature.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.AssignRoleToUserRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.RoleResponse;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RoleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.repository.RoleRepository;
import vn.edu.hcmuaf.fit.elearning.feature.user.UserEntity;
import vn.edu.hcmuaf.fit.elearning.feature.user.UserRepository;
import vn.edu.hcmuaf.fit.elearning.feature.user.UserService;
import vn.edu.hcmuaf.fit.elearning.feature.user.dto.req.UserChangePasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.user.dto.req.UserCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.user.dto.req.UserUpdateInfoRequest;
import vn.edu.hcmuaf.fit.elearning.feature.user.dto.res.UserPageResponse;
import vn.edu.hcmuaf.fit.elearning.feature.user.dto.res.UserResponse;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public long createUser(UserCreationRequest req) {
        log.info("Creating user {}", req.getEmail());

        UserEntity user = new UserEntity();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setFullName(req.getFullName());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setGender(req.getGender());
        user.setDateOfBirth(req.getDateOfBirth());
        user.setStatus(req.getStatus());

        userRepository.save(user);
        log.info("User {} created successfully", req.getEmail());

        return user.getId();
    }

    @Override
    public UserResponse getUserById(Long id) {
        UserEntity user = this.findUserById(id);
        return this.convertToResponse(user);
    }

    @Override
    public long updateUserInfoById(UserUpdateInfoRequest userUpdateRequest) {
        log.info("Updating user {}", userUpdateRequest.getId());
        UserEntity user = this.findUserById(userUpdateRequest.getId());

        user.setFullName(userUpdateRequest.getFullName() != null ? userUpdateRequest.getFullName() : user.getFullName());
        user.setGender(userUpdateRequest.getGender() != null ? userUpdateRequest.getGender() : user.getGender());
        user.setDateOfBirth(userUpdateRequest.getDateOfBirth() != null ? userUpdateRequest.getDateOfBirth() : user.getDateOfBirth());

        //save to db
        userRepository.save(user);
        log.info("User {} updated successfully", user.getId());
        return user.getId();
    }

    @Override
    public long changePassword(UserChangePasswordRequest userChangePasswordRequest) {
        log.info("Changing password for user {}", userChangePasswordRequest.getId());
        UserEntity user = this.findUserById(userChangePasswordRequest.getId());

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException(Translator.translate("user.change-password.old-password-not-match"));
        }
        user.setPassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
        userRepository.save(user);
        log.info("Password for user {} changed successfully", user.getId());
        return user.getId();
    }

    @Override
    public UserPageResponse getAllUsers(String keyword, String sort, int pageNo, int pageSize, boolean isDeleted) {
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        //Sort
        if(StringUtils.hasLength(sort)){
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");//columName:asc|desc
            Matcher matcher = pattern.matcher(sort);
            if(matcher.find()){
                String column = matcher.group(1);
                if(matcher.group(3).equalsIgnoreCase("asc")){
                    order = new Sort.Order(Sort.Direction.ASC, column);
                }else {
                    order = new Sort.Order(Sort.Direction.DESC, column);
                }
            }
        }
        int pageNoTemp = 0;
        if(pageNo > 0){
            pageNoTemp = pageNo -1;
        }

        //Pagging
        Pageable pageable = PageRequest.of(pageNoTemp, pageSize, Sort.by(order));
        Page<UserEntity> userPage;
        if(StringUtils.hasLength(keyword)){
            //call search method
            keyword = "%"+keyword.toLowerCase()+"%";
            userPage = userRepository.searchByKeyword(keyword, isDeleted, pageable);
        }else{
            userPage = userRepository.findByIsDeleted(isDeleted, pageable);
        }

        //Convert to response
        List<UserResponse> userResponses = userPage.getContent().stream().map(this::convertToResponse).toList();
        return UserPageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .users(userResponses)
                .build();
    }

    @Override
    public long deleteUserById(Long id) {
        UserEntity user = this.findUserById(id);
        log.info("Deleting user {}", user.getEmail());
        user.setDeleted(true);
        userRepository.save(user);
        log.info("User {} deleted successfully", user.getEmail());
        return user.getId();
    }

    @Override
    public long restoreUserById(Long id) {
        UserEntity user = this.findUserById(id);
        log.info("Restoring user {}", user.getEmail());
        user.setDeleted(false);
        userRepository.save(user);
        log.info("User {} restored successfully", user.getEmail());
        return user.getId();
    }

    @Override
    public UserResponse getMe() {
        log.info("Getting current user");
        // Get the current user from the security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new ResourceNotFoundException(Translator.translate("user.not-found"));
        }
        String email = authentication.getName();
        UserEntity userEntity = this.userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException(Translator.translate("user.not-found")));
        log.info("User {} retrieved successfully", userEntity.getEmail());
        return this.convertToResponse(userEntity);
    }

    @Override
    public long assignRoleToUser(AssignRoleToUserRequest req) {
        // get user by id
        UserEntity user = this.findUserById(req.getUserId());
        // check role ids
        if(req.getRoleIds() == null ){
            throw new IllegalArgumentException(Translator.translate("user.role-ids.empty"));
        }

        //set roles to empty
        user.setRoles(Set.of());

        Set<RoleEntity> roles = new HashSet<>();
        for(Long roleId : req.getRoleIds()){
            RoleEntity role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new ResourceNotFoundException(Translator.translate("role.not-found")));
            roles.add(role);
        }
        // set roles to user
        user.setRoles(roles);
        userRepository.save(user);
        log.info("User {} assigned roles successfully", user.getEmail());
        return user.getId();
    }

    private UserEntity findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Translator.translate("user.not-found")));
    }
    private UserResponse convertToResponse(UserEntity user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .phoneNumber(user.getPhoneNumber())
                .updatedAt(user.getUpdatedAt())
                .roles(user.getRoles().stream().map(r -> RoleResponse.builder()
                        .id(r.getId())
                        .name(r.getName())
                        .description(r.getDescription())
                        .build()).toList())
                .build();
    }

}
