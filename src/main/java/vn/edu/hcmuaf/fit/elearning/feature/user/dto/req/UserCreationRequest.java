package vn.edu.hcmuaf.fit.elearning.feature.user.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import vn.edu.hcmuaf.fit.elearning.common.enums.Gender;
import vn.edu.hcmuaf.fit.elearning.common.enums.UserStatus;

import java.io.Serializable;
import java.time.LocalDate;
@Getter
@ToString
public class UserCreationRequest implements Serializable {
    @NotBlank(message = "fullName must be not blank")
    private String fullName;

    @NotNull(message = "gender must be not blank")
    private Gender gender;

    @NotNull(message = "dateOfBirth must be not blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @NotBlank(message = "phoneNumber must be not blank")
    private String phoneNumber;

    @NotBlank(message = "email must be not blank")
    @Email(message = "email không hợp lệ")
    private String email;

    @NotBlank(message = "password must be not blank")
    private String password;

    @NotNull(message = "status must be not blank")
    private UserStatus status;
}
