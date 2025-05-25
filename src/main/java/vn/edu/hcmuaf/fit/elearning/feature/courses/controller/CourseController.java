package vn.edu.hcmuaf.fit.elearning.feature.courses.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get list of courses", description = "Retrieves a paginated list of published courses, optionally filtered by category.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination or sort parameters"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseDto getCourses(
            @Parameter(description = "Sort criteria (e.g., 'name:asc' or 'price:desc')", example = "name:asc")
            @RequestParam(defaultValue = "id:asc") String sort,
            @Parameter(description = "Page number (1-based)", example = "1")
            @RequestParam(defaultValue = "1") @Min(1) int pageNo,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) int pageSize,
            @Parameter(description = "Category ID to filter courses (optional)", example = "1")
            @RequestParam(required = false) Long categoryId
    ) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("course.get.success"))
                .data(courseService.getCourses(sort, pageNo, pageSize, categoryId))
                .build();
    }
}
