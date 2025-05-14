package vn.edu.hcmuaf.fit.elearning.feature.courses.service;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateLessonRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateLessonRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.LessonResponseDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.LessonEntity;

import java.util.List;

public interface LessonService {
    Long createLesson(CreateLessonRequestDto req);

    Long updateLesson( UpdateLessonRequestDto req);

    LessonEntity findById(Long id);

    LessonResponseDto findByIdDetail(Long id);

    Long addFile(Long id, List<MultipartFile> file);

    Long removeFile(Long lessonId, String fileName);

    Long deleteLesson(Long id);

    Long permanentlyDeleteLesson(Long id);

    List<LessonResponseDto> findAllByModuleId(Long moduleId);
}
