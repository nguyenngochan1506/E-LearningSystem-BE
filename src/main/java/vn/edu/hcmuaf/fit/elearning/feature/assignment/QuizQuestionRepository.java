package vn.edu.hcmuaf.fit.elearning.feature.assignment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestionEntity, Long> {
}
