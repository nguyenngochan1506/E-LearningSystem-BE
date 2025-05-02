package vn.edu.hcmuaf.fit.elearning.feature.auth.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.elearning.feature.auth.entity.RedisTokenEntity;

@Repository
public interface RedisTokenRepository extends CrudRepository<RedisTokenEntity, String> {
}
