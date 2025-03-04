package vn.edu.ngochandev.feature.user.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserChangePasswordRequest {
    @NotNull(message = "userId must be not null")
    @Min(value = 1, message = "userId must be equals or greater than 1")
    private Long id;
    @NotBlank(message = "oldPassword must be not blank")
    private String oldPassword;
    @NotBlank(message = "newPassword must be not blank")
    private String newPassword;
}
