package vn.edu.ngochandev.feature.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Builder
@ToString
public class TokenResponse implements Serializable {
    private String accessToken;
    private String refreshToken;
    private long userId;
}
