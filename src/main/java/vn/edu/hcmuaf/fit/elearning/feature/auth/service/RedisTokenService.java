package vn.edu.hcmuaf.fit.elearning.feature.auth.service;

import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RedisTokenEntity;

public interface RedisTokenService {
    String saveToken(RedisTokenEntity token);

    void deleteToken(String id);

    RedisTokenEntity getToken(String id);
}
