package vn.edu.hcmuaf.fit.elearning.feature.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import vn.edu.hcmuaf.fit.elearning.common.enums.Gender;
import vn.edu.hcmuaf.fit.elearning.common.enums.UserStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
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
}
