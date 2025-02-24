package vn.edu.ngochandev.feature.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import vn.edu.ngochandev.common.Gender;
import vn.edu.ngochandev.common.UserType;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class UserCreationRequest implements Serializable {
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String password;
    private UserType userType;
}
