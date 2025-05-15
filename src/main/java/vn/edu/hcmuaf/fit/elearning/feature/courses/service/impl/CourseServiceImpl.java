package vn.edu.hcmuaf.fit.elearning.feature.courses.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CategoryEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CourseEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.repository.CourseRepository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CategoryService;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CourseService;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileService;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserEntity;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserService;

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
}
