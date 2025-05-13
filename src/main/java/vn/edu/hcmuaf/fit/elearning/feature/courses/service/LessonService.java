package vn.edu.hcmuaf.fit.elearning.feature.courses.service;

import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateLessonRequestDto;

public interface LessonService {
    Long createLesson(CreateLessonRequestDto req);
}
