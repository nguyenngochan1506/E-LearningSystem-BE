package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class AuthResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private long userId;
}
