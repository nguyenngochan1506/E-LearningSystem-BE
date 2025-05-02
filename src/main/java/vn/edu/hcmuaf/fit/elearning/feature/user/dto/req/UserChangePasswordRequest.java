package vn.edu.hcmuaf.fit.elearning.feature.user.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class UserChangePasswordRequest  implements Serializable {
    @NotNull(message = "id must be not blank")
    private Long id;
    @NotBlank(message = "oldPassword must be not blank")
    private String oldPassword;
    @NotBlank(message = "newPassword must be not blank")
    private String newPassword;
}
