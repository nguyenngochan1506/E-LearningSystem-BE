package vn.edu.hcmuaf.fit.elearning.feature.courses.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CourseEntity;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    Page<CourseEntity> findByCategoryIdAndIsPublishedAndIsDeleted(Long categoryId, boolean b, boolean b1, Pageable pageable);

    Page<CourseEntity> findByIsPublishedAndIsDeleted(boolean b, boolean b1, Pageable pageable);
}
