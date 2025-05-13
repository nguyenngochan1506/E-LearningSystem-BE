package vn.edu.hcmuaf.fit.elearning.feature.courses.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateLessonRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.LessonEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.ModuleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.repository.LessonRepository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.LessonService;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.ModuleService;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {
    private final LessonRepository lessonRepository;
    private final ModuleService moduleService;
    private final FileService fileService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createLesson( CreateLessonRequestDto req) {
        LessonEntity lesson = new LessonEntity();
        lesson.setName(req.getName());
        lesson.setContent(req.getContent());
        lesson.setNumber(req.getNumber());

        //check module
        ModuleEntity module = moduleService.findById(req.getModuleId());
        lesson.setModule(module);
        //file
        List<MultipartFile> files = req.getFiles();
        StringBuilder fileUrls = new StringBuilder();
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                String fileUrl = fileService.saveFile(file);
                fileUrls.append(fileUrl).append(",");
            }
            // Remove the trailing comma
            fileUrls.deleteCharAt(fileUrls.length() - 1);
            lesson.setFileUrl(fileUrls.toString());
        }
        if(req.getVideoFile() != null && !req.getVideoFile().isEmpty() ){
            String videoUrl = fileService.saveFile(req.getVideoFile());
            lesson.setVideoUrl(videoUrl);
        }else {
            lesson.setVideoUrl(req.getVideoUrl());
        }
        return lessonRepository.save(lesson).getId();
    }
}
