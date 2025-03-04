package vn.edu.ngochandev.feature.user.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.ToString;
import vn.edu.ngochandev.common.Gender;
import vn.edu.ngochandev.common.UserType;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@ToString
public class UserCreationRequest implements Serializable {

    @NotBlank(message = "fullName must be not blank")
    private String fullName;

    @NotBlank(message = "gender must be not blank")
    private Gender gender;

    @NotBlank(message = "dateOfBirth must be not blank")
    private LocalDate dateOfBirth;

    @NotBlank(message = "phoneNumber must be not blank")
    private String phoneNumber;

    @NotBlank(message = "email must be not blank")
    private String email;

    @NotBlank(message = "password must be not blank")
    private String password;

    @NotBlank(message = "userType must be not blank")
    private UserType userType;

    @NotBlank(message = "className must be not blank")
    private String className;
}
