package vn.edu.hcmuaf.fit.elearning.feature.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.QuizEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.QuizQuestionEntity;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestionEntity, Long> {
}
