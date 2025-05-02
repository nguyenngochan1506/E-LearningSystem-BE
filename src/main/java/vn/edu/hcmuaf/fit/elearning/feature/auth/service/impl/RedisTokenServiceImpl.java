package vn.edu.hcmuaf.fit.elearning.feature.auth.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RedisTokenEntity;
import vn.edu.hcmuaf.fit.elearning.feature.auth.repository.RedisTokenRepository;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.RedisTokenService;

@Service
@RequiredArgsConstructor
public class RedisTokenServiceImpl implements RedisTokenService {
    private final RedisTokenRepository redisTokenRepository;
    @Override
    public String saveToken(RedisTokenEntity token) {
        return redisTokenRepository.save(token).getId();
    }

    @Override
    public void deleteToken(String id) {
        redisTokenRepository.deleteById(id);
    }

    @Override
    public RedisTokenEntity getToken(String id) {
        return redisTokenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(Translator.translate("auth.token.not_found")));
    }
}
