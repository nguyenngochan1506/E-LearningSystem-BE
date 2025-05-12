package vn.edu.hcmuaf.fit.elearning.feature.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.ModuleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.QuizAnswerEntity;

@Repository
public interface QuizAnswerRepository extends JpaRepository<QuizAnswerEntity, Long> {
}
