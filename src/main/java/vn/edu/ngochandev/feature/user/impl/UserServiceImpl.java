package vn.edu.ngochandev.feature.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import vn.edu.ngochandev.common.UserStatus;
import vn.edu.ngochandev.exception.ResourceNotFoundException;
import vn.edu.ngochandev.feature.user.dto.request.UserChangePasswordRequest;
import vn.edu.ngochandev.feature.user.dto.request.UserCreationRequest;
import vn.edu.ngochandev.feature.user.dto.request.UserUpdateRequest;
import vn.edu.ngochandev.feature.user.dto.response.UserPageResponse;
import vn.edu.ngochandev.feature.user.dto.response.UserResponse;
import vn.edu.ngochandev.feature.user.UserEntity;
import vn.edu.ngochandev.feature.user.UserRepository;
import vn.edu.ngochandev.feature.user.UserService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j(topic = "USER-SERVICE")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserPageResponse findAll(String keyword, String sort, int page, int size) {

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
        int pageNo = 0;
        if(page > 0){
            pageNo = page -1;
        }

        //Pagging
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(order));
        Page<UserEntity> userPage;
        if(StringUtils.hasLength(keyword)){
            //call search method
            keyword = "%"+keyword.toLowerCase()+"%";
            userPage = userRepository.searchByKeyword(keyword, pageable);
        }else{
            userPage = userRepository.findAll(pageable);
        }
        return convertToUserPageResponse(page, size, userPage);
    }

    /**
     * Convert Page<UserEntity> to UserPageResponse
     * @param page: currentPage
     * @param size: number of elements in db
     * @param userPage: Page<UserEntity>
     * @return UserPageResponse
     */
    private static UserPageResponse convertToUserPageResponse(int page, int size, Page<UserEntity> userPage) {
        //convert to userResponse
        List<UserResponse> userResponses = userPage.map(e -> UserResponse.builder()
                .id(e.getId())
                .phoneNumber(e.getPhoneNumber())
                .email(e.getEmail())
                .dateOfBirth(e.getDateOfBirth())
                .userStatus(e.getStatus())
                .fullName(e.getFullName())
                .gender(e.getGender())
                .userName(e.getUserName())
                .build()).toList();

        return UserPageResponse.builder()
                .pageNumber(page)
                .pageSize(size)
                .totalPages(userPage.getTotalPages())
                .totalElements(userPage.getTotalElements())
                .users(userResponses)
                .build();
    }

    @Override
    public UserResponse getUserDetailById(Long id) {
        UserEntity user = this.getUserById(id);
        return UserResponse.builder()
                .userName(user.getUserName())
                .userStatus(user.getStatus())
                .id(user.getId())
                .gender(user.getGender())
                .fullName(user.getFullName())
                .dateOfBirth(user.getDateOfBirth())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
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
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setUserType(req.getUserType());
        user.setClassName(req.getClassName());

        userRepository.save(user);

        log.info("Saved user {}", req);
        return user.getId();
    }

    @Override
    public void updateUser(UserUpdateRequest req) {
        log.info("Updating user {}", req);
        //get user by id
        UserEntity user = this.getUserById(req.getId());
        //update data
        user.setFullName(req.getFullName());
        user.setGender(req.getGender());
        user.setDateOfBirth(req.getDateOfBirth());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setEmail(req.getEmail());
        user.setUserName(extractStudentIdFromEmail(req.getEmail()));
        user.setClassName(req.getClassName());
        // save to db
        userRepository.save(user);

        log.info("Updated user {}", req);
    }

    @Override
    public void deleteUser(Long id) {
        log.info("Deleting user {}", id);
        //get user
        UserEntity user = this.getUserById(id);
        //set status
        user.setStatus(UserStatus.INACTIVE);
        //save to db
        userRepository.save(user);
        log.info("Deleted user {}", id);
    }

    @Override
    public void changePassword(UserChangePasswordRequest req) {
        log.info("Changing password {}", req);
        //get user by id
        UserEntity user = this.getUserById(req.getId());
        String oldPass = user.getPassword();
        if(!passwordEncoder.matches(req.getOldPassword(), oldPass)) throw new RuntimeException("Old password does not match");
        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        log.info("Changed password {}", req);
    }

    private String extractStudentIdFromEmail(String email) {
        String[] parts = email.split("@");
        return parts[0];
    }
    private UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found!"));
    }
}

