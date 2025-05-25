package vn.edu.hcmuaf.fit.elearning.feature.courses.service;

import jakarta.validation.Valid;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.CoursePageResponse;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.CourseResponseDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CourseEntity;

public interface CourseService {
    Long createCourse(CreateCourseRequestDto req);
    CourseEntity findById(Long id);

    Long updateCourse( UpdateCourseRequestDto req);

    CourseResponseDto getCourse(Long id);
    CoursePageResponse getCourses(String sort, Integer pageNo, Integer pageSize, Long categoryId);
}
