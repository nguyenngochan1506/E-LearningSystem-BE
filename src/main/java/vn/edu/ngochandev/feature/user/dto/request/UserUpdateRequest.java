package vn.edu.ngochandev.feature.user.dto.request;

import lombok.Getter;
import lombok.ToString;
import vn.edu.ngochandev.common.Gender;
import vn.edu.ngochandev.common.UserStatus;
import vn.edu.ngochandev.common.UserType;

import java.time.LocalDate;

@Getter
@ToString
public class UserUpdateRequest {
    private long id;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String className;
}
