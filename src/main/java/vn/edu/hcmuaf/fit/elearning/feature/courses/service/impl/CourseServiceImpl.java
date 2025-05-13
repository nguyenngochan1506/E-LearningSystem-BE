package vn.edu.hcmuaf.fit.elearning.feature.courses.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateCourseRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CourseEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.repository.CourseRepository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CourseService;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final FileService fileService;
    private final CourseRepository courseRepository;
    @Override
    public Long createCourse(CreateCourseRequestDto req) {
        CourseEntity course = new CourseEntity();
        course.setName(req.getName());
        course.setDescription(req.getDescription());
        course.setPrice(req.getPrice());
        course.setIsPublished(req.getIsPublished());
        //save thumbnail to s3
        String thumbnailUrl = fileService.saveFile(req.getThumbnail());
        course.setThumbnail(thumbnailUrl);

        //save course to database
        return courseRepository.save(course).getId();
    }

    @Override
    public CourseEntity findById(Long id) {
        return courseRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(Translator.translate("course.not.found")));
    }
}
