package vn.edu.hcmuaf.fit.elearning.feature.auth.dto.res;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import vn.edu.hcmuaf.fit.elearning.common.BaseResponse;

import java.io.Serializable;

@SuperBuilder
@Getter
public class AuthResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private long userId;
}
