package vn.edu.ngochandev.feature.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import vn.edu.ngochandev.common.Platform;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class SignInRequest implements Serializable {
    @NotBlank(message = "username  must be not blank")
    private String username;
    @NotBlank(message = "password  must be not blank")
    private String password;
    @NotNull(message = "platform  must be not null")
    private Platform platform;
    private String deviceToken;
}
