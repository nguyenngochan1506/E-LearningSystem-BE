package vn.edu.hcmuaf.fit.elearning.feature.users.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import vn.edu.hcmuaf.fit.elearning.common.enums.Gender;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
public class UserUpdateInfoRequest implements Serializable {
    @NotNull(message = "id must be not blank")
    private Long id;
    
    @NotBlank(message = "fullName must be not blank")
    private String fullName;

    @NotNull(message = "gender must be not blank")
    private Gender gender;

    @NotNull(message = "dateOfBirth must be not blank")
    private LocalDate dateOfBirth;

}
