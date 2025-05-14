package vn.edu.hcmuaf.fit.elearning.feature.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.LessonEntity;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
}
