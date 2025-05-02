package vn.edu.hcmuaf.fit.elearning.feature.auth.entity;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("RedisToken")
public class RedisTokenEntity implements Serializable {
    private String id;
    private String accessToken;
    private String refreshToken;
    private String resetToken;
}
