package vn.edu.ngochandev.feature.user.dto.request;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserChangePasswordRequest {
    private long id;
    private String oldPassword;
    private String newPassword;
}
