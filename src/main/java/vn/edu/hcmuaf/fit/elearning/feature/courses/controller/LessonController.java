package vn.edu.hcmuaf.fit.elearning.feature.courses.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateLessonRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.LessonService;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@Tag(name = "LESSON", description = "Lesson API")
public class LessonController {
    private final LessonService lessonService;
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseDto createLesson(@ModelAttribute @Valid CreateLessonRequestDto req) {
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("lesson.create.success"))
                .data(lessonService.createLesson(req))
                .build();
    }
}
