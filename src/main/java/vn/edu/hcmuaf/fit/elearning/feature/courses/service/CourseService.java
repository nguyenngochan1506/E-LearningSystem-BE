package vn.edu.hcmuaf.fit.elearning.feature.courses.service;

import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.CreateCourseRequestDto;

public interface CourseService {
    Long createCourse(CreateCourseRequestDto req);
}
