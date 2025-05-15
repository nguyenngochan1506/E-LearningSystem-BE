package vn.edu.hcmuaf.fit.elearning.feature.courses.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateLessonRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateLessonRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.LessonService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/lessons")
@RequiredArgsConstructor
@Tag(name = "LESSON", description = "Lesson API")
public class LessonController {
    private final LessonService lessonService;

    @PatchMapping("/restore/{id}")
    public ResponseDto restoreLesson(@PathVariable("id") Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("lesson.restore.success"))
                .data(lessonService.restoreLesson(id))
                .build();
    }

    @GetMapping("/{id}")
    public ResponseDto getLesson(@PathVariable Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("lesson.get.success"))
                .data(lessonService.findByIdDetail(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseDto deleteLesson(@PathVariable Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("lesson.delete.success"))
                .data(lessonService.deleteLesson(id))
                .build();
    }
    @DeleteMapping("permanently/{id}")
    public ResponseDto permanentlyDeleteLesson(@PathVariable Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("lesson.delete.permanently.success"))
                .data(lessonService.permanentlyDeleteLesson(id))
                .build();
    }
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseDto createLesson(@ModelAttribute @Valid CreateLessonRequestDto req) {
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("lesson.create.success"))
                .data(lessonService.createLesson(req))
                .build();
    }
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ResponseDto updateLesson(@ModelAttribute @Valid UpdateLessonRequestDto req) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("lesson.update.success"))
                .data(lessonService.updateLesson(req))
                .build();
    }
    @PatchMapping("/add-file")
    public ResponseDto addFile(@RequestParam("lessonId") Long lessonId,
                               @RequestParam("files") List<MultipartFile> files) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("lesson.add.file.success"))
                .data(lessonService.addFile(lessonId, files))
                .build();
    }
    @PatchMapping("/remove-file")
    public ResponseDto removeFile(@RequestParam("lessonId") Long lessonId,
                                  @RequestParam("fileUrl") String fileUrl) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("lesson.remove.file.success"))
                .data(lessonService.removeFile(lessonId, fileUrl))
                .build();
    }
}
