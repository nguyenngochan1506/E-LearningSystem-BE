package vn.edu.ngochandev.feature.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;
import vn.edu.ngochandev.common.Gender;

import java.time.LocalDate;

@Getter
@ToString
public class UserUpdateRequest {
    @NotNull(message = "id must me not null")
    @Min(value = 1, message = "userId must be equals or greater than 1")
    private Long id;

    private String fullName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String email;

    private String className;
}
