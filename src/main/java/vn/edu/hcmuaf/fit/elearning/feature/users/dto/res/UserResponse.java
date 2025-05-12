package vn.edu.hcmuaf.fit.elearning.feature.users.dto.res;

import lombok.*;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.BaseResponse;
import vn.edu.hcmuaf.fit.elearning.common.enums.Gender;
import vn.edu.hcmuaf.fit.elearning.common.enums.UserStatus;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res.RoleResponse;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@SuperBuilder
public class UserResponse extends BaseResponse {
    private Long id;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private UserStatus status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RoleResponse> roles;
}
