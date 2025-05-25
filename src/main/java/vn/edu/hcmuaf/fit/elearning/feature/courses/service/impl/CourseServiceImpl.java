package vn.edu.hcmuaf.fit.elearning.feature.courses.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.*;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CategoryEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CourseEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.repository.CourseRepository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CategoryService;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CourseService;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileService;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserEntity;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserService;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.res.UserResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final FileService fileService;
    private final CourseRepository courseRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public Long createCourse(CreateCourseRequestDto req) {
        CourseEntity course = new CourseEntity();
        course.setName(req.getName());
        course.setDescription(req.getDescription());
        course.setPrice(req.getPrice());
        course.setIsPublished(req.getIsPublished());

        //save category to course
        Long categoryId = req.getCategoryId();
        CategoryEntity category = categoryService.findById(categoryId);
        course.setCategory(category);

        //save thumbnail to s3
        String thumbnailUrl = fileService.saveFile(req.getThumbnail());
        course.setThumbnail(thumbnailUrl);
        //save author to course
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String author = auth.getName();
        UserEntity user = userService.findByEmail(author);
        course.setTeacher(user);

        //save course to database
        return courseRepository.save(course).getId();
    }

    @Override
    public CourseEntity findById(Long id) {
        return courseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(Translator.translate("course.not.found")));
    }

    @Override
    public Long updateCourse(UpdateCourseRequestDto req) {
        log.info("Updating course {}", req.getId());
        CourseEntity course = this.findById(req.getId());
        course.setName(req.getName() != null ? req.getName() : course.getName());
        course.setDescription(req.getDescription() != null ? req.getDescription() : course.getDescription());
        course.setPrice(req.getPrice() != null ? req.getPrice() : course.getPrice());
        course.setIsPublished(req.getIsPublished() != null ? req.getIsPublished() : course.getIsPublished());
        //save category to course
        Long categoryId = req.getCategoryId();
        if (categoryId != null) {
            CategoryEntity category = categoryService.findById(categoryId);
            course.setCategory(category);
        }
        //save thumbnail to s3
        if (req.getThumbnail() != null) {
            //remove old thumbnail
            log.info("Deleting old thumbnail from s3");
            fileService.deleteFileByUrl(course.getThumbnail());

            //save new thumbnail
             log.info("Saving new thumbnail to s3");
            String thumbnailUrl = fileService.saveFile(req.getThumbnail());
            course.setThumbnail(thumbnailUrl);
        }
        return courseRepository.save(course).getId();
    }

    @Override
    public CourseResponseDto getCourse(Long id) {
        CourseEntity course = this.findById(id);

        return convertToResponse(course, true);
    }

    @Override
    public CoursePageResponse getCourses(String sort, Integer pageNo, Integer pageSize, Long categoryId) {
        log.info("Fetching courses with sort: {}, pageNo: {}, pageSize: {}, categoryId: {}",
                sort, pageNo, pageSize, categoryId);

        // Xử lý sắp xếp
        Sort.Order order = new Sort.Order(Sort.Direction.ASC, "id");
        if (sort != null && !sort.isEmpty()) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(asc|desc)", Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String column = matcher.group(1);
                String direction = matcher.group(3);
                order = new Sort.Order(
                        direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
                        column
                );
            }
        }

        // Xử lý phân trang
        int pageNoTemp = pageNo > 0 ? pageNo - 1 : 0;
        Pageable pageable = PageRequest.of(pageNoTemp, pageSize, Sort.by(order));

        // Truy vấn khóa học
        Page<CourseEntity> coursePage;
        if (categoryId != null) {
            categoryService.findById(categoryId);
            coursePage = courseRepository.findByCategoryIdAndIsPublishedAndIsDeleted(
                    categoryId, true, false, pageable
            );
        } else {
            coursePage = courseRepository.findByIsPublishedAndIsDeleted(true, false, pageable);
        }

        // Chuyển đổi sang DTO
        List<?> courses = coursePage.getContent().stream()
                .map(course -> CourseResponseDto.builder()
                        .id(course.getId())
                        .name(course.getName())
                        .description(course.getDescription())
                        .price(course.getPrice())
                        .thumbnail(course.getThumbnail())
                        .category(course.getCategory() != null
                                ? CategoryResponseDto.builder()
                                .id(course.getCategory().getId())
                                .name(course.getCategory().getName())
                                .build()
                                : null)
                        .teacher(course.getTeacher() != null
                                ? UserResponse.builder()
                                .id(course.getTeacher().getId())
                                .fullName(course.getTeacher().getFullName())
                                .build()
                                : null)
                        .createdAt(course.getCreatedAt())
                        .updatedAt(course.getUpdatedAt())
                        .build()
                )
                .toList();

        // Trả về CoursePageResponse
        return CoursePageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalElements(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .courses((List<CourseResponseDto>) courses)
                .build();
    }

    private CourseResponseDto convertToResponse(CourseEntity course, boolean attachLessons) {

        List<ModuleResponseDto> modules = new ArrayList<>();
        course.getModules().forEach(module -> {
            List<LessonResponseDto> lessons = new ArrayList<>();
            if(attachLessons){
                module.getLessons().forEach(lesson -> {
                    LessonResponseDto lessonResponseDto = LessonResponseDto.builder()
                            .id(lesson.getId())
                            .name(lesson.getName())
                            .videoUrl(lesson.getVideoUrl())
                            .content(lesson.getContent())
                            .createdBy(lesson.getCreatedBy())
                            .createdAt(lesson.getCreatedAt())
                            .updatedBy(lesson.getUpdatedBy())
                            .updatedAt(lesson.getUpdatedAt())
                            .build();
                    lessons.add(lessonResponseDto);
                });
            }
            ModuleResponseDto moduleResponseDto = ModuleResponseDto.builder()
                    .id(module.getId())
                    .name(module.getName())
                    .description(module.getDescription())
                    .createdBy(module.getCreatedBy())
                    .createdAt(module.getCreatedAt())
                    .updatedBy(module.getUpdatedBy())
                    .updatedAt(module.getUpdatedAt())
                    .lessons(lessons)
                    .build();
            modules.add(moduleResponseDto);
        });

        return CourseResponseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .price(course.getPrice())
                .thumbnail(course.getThumbnail())
                .isPublished(course.getIsPublished())
                .modules(modules)
                .createdAt(course.getCreatedAt())
                .createdBy(course.getCreatedBy())
                .updatedAt(course.getUpdatedAt())
                .updatedBy(course.getUpdatedBy())
                .build();
    }
}
