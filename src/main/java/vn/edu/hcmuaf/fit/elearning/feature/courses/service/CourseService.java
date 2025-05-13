package vn.edu.hcmuaf.fit.elearning.feature.courses.service;

import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CourseEntity;

public interface CourseService {
    Long createCourse(CreateCourseRequestDto req);
    CourseEntity findById(Long id);
}
