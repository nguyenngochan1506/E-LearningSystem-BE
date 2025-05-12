package vn.edu.hcmuaf.fit.elearning.feature.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.LessonEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.ModuleEntity;

@Repository
public interface ModuleRepository extends JpaRepository<ModuleEntity, Long> {
}
