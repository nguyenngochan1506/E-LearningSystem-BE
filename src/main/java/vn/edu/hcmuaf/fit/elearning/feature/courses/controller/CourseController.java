package vn.edu.hcmuaf.fit.elearning.feature.courses.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CourseService;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto createCourse( @ModelAttribute @Valid CreateCourseRequestDto courseDto) {
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("course.create.success"))
                .data(courseService.createCourse(courseDto))
                .build();
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseDto updateCourse(@ModelAttribute @Valid UpdateCourseRequestDto req) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("course.update.success"))
                .data(courseService.updateCourse(req))
                .build();
    }
}
