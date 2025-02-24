package vn.edu.ngochandev.feature.user.dto.response;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String email;
    private String userName;
    private String userStatus;
}
