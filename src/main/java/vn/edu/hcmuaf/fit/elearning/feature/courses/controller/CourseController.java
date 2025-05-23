package vn.edu.hcmuaf.fit.elearning.feature.courses.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CourseService;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "COURSE", description = "Course API")
public class CourseController {
    private final CourseService courseService;
    @GetMapping("{id}")
    public ResponseDto getCourse(@PathVariable Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("course.get.success"))
                .data(courseService.getCourse(id))
                .build();
    }
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
