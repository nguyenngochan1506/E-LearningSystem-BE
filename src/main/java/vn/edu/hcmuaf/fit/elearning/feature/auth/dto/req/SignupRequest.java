package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vn.edu.hcmuaf.fit.elearning.common.enums.Gender;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class SignupRequest implements Serializable {
    @NotBlank(message = "error.validate.not-blank")
    private String fullName;

    @NotNull(message = "error.validate.not-blank")
    private Gender gender;

    @NotNull(message = "error.validate.not-blank")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @NotBlank(message = "error.validate.not-blank")
    private String phoneNumber;

    @NotBlank(message = "error.validate.not-blank")
    @Email(message = "error.validate.invalid")
    private String email;

    @NotBlank(message = "error.validate.not-blank")
    private String password;
}
