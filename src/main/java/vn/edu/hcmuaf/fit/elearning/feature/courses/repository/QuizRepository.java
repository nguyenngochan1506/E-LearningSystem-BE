package vn.edu.hcmuaf.fit.elearning.feature.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.QuizAnswerEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.QuizEntity;

@Repository
public interface QuizRepository extends JpaRepository<QuizEntity, Long> {
}
