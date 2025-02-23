package vn.edu.ngochandev.dto.response;

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
    private String phone;
    private String email;
    private String userName;
    private String userStatus;
}
