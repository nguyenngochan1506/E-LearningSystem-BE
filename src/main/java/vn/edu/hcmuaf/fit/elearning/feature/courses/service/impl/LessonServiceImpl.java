package vn.edu.hcmuaf.fit.elearning.feature.courses.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateLessonRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateLessonRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.LessonResponseDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.LessonEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.ModuleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.repository.LessonRepository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.LessonService;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.ModuleService;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileEntity;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileService;
import vn.edu.hcmuaf.fit.elearning.feature.file.dto.response.FileResponseDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        log.info("Create lesson with name: {}", req.getName());
        LessonEntity lesson = new LessonEntity();
        lesson.setName(req.getName());
        lesson.setContent(req.getContent());
        lesson.setNumber(req.getNumber());
        lesson.setDuration(req.getDuration());

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
        log.info("Created lesson: {}", lesson.getId());
        return lessonRepository.save(lesson).getId();
    }

    @Override
    public Long updateLesson(UpdateLessonRequestDto req) {
        LessonEntity lesson = this.findById(req.getId());
        log.info("Update lesson with id: {}", lesson.getId());
        lesson.setDuration(req.getDuration() != null ? req.getDuration() : lesson.getDuration());
        lesson.setName(req.getName() != null ? req.getName() : lesson.getName());
        lesson.setContent(req.getContent() != null ? req.getContent() : lesson.getContent());
        lesson.setNumber(req.getNumber() != null ? req.getNumber() : lesson.getNumber());
        //video file
        if(req.getVideoFile() != null && !req.getVideoFile().isEmpty() ){
            // Delete the old video file if it exists
            if (lesson.getVideoUrl() != null) {
                fileService.deleteFileByUrl(lesson.getVideoUrl());
            }
            String videoUrl = fileService.saveFile(req.getVideoFile());
            lesson.setVideoUrl(videoUrl);
        }else {
            lesson.setVideoUrl(req.getVideoUrl() != null ? req.getVideoUrl() : lesson.getVideoUrl());
        }
        this.lessonRepository.save(lesson);
        log.info("Lesson updated with id: {}", lesson.getId());
        return req.getId();
    }

    @Override
    public LessonEntity findById(Long id) {
        return this.lessonRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("lesson.not.found")));
    }

    @Override
    public LessonResponseDto findByIdDetail(Long id) {
        LessonEntity lesson = this.findById(id);
        log.info("Get lesson with id: {}", lesson.getId());
        return convertToResponseDto(lesson, true);
    }

    private LessonResponseDto convertToResponseDto(LessonEntity lesson,boolean attachFiles ) {
       List<FileResponseDto> files = new ArrayList<>();
       if( attachFiles && lesson.getFileUrl() != null && !lesson.getFileUrl().isEmpty()){
              String[] fileUrls = lesson.getFileUrl().split(",");
              for (String fileUrl : fileUrls) {
                FileEntity file = fileService.findByUrl(fileUrl);
                files.add(FileResponseDto.builder()
                            .id(file.getId())
                            .fileUrl(file.getUrl())
                            .fileName(file.getName())
                            .fileType(file.getType())
                            .size(file.getSize())
                            .build());
              }
       }

        return LessonResponseDto.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .content(lesson.getContent())
                .number(lesson.getNumber())
                .duration(lesson.getDuration())
                .files(files)
                .videoUrl(lesson.getVideoUrl())
                .createdAt(lesson.getCreatedAt())
                .updatedAt(lesson.getUpdatedAt())
                .createdBy(lesson.getCreatedBy())
                .updatedBy(lesson.getUpdatedBy())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addFile(Long id, List<MultipartFile> file) {
        LessonEntity lesson = this.findById(id);
        log.info("Add file to lesson with id: {}", lesson.getId());
        StringBuilder newFileUrls = new StringBuilder();
        for (MultipartFile multipartFile : file) {
            String fileUrl = fileService.saveFile(multipartFile);
            newFileUrls.append(fileUrl).append(",");
        }
        StringBuilder oldFileUrl = new StringBuilder(lesson.getFileUrl());
        if(!oldFileUrl.isEmpty()){
            // Remove the trailing comma
            newFileUrls.append(oldFileUrl);
        }else {
            // Remove the trailing comma
            newFileUrls.deleteCharAt(newFileUrls.length() - 1);
        }
        // Save the lesson with the new file URL
        lesson.setFileUrl(newFileUrls.toString());

        this.lessonRepository.save(lesson);
        log.info("File added to lesson with id: {}", lesson.getId());
        return lesson.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long removeFile(Long lessonId, String fileUrl) {
        LessonEntity lesson = this.findById(lessonId);
        log.info("Remove file from lesson with id: {}", lesson.getId());

        String oldFileUrl = lesson.getFileUrl();
        String[] fileUrls = oldFileUrl.split(",");

        StringBuilder newFileUrls = new StringBuilder();
        for (String url : fileUrls) {
            if (!url.equals(fileUrl)) {
                newFileUrls.append(url).append(",");
            }
        }
        // Remove the trailing comma
        if (!newFileUrls.isEmpty()) {
            newFileUrls.deleteCharAt(newFileUrls.length() - 1);
        }
        lesson.setFileUrl(newFileUrls.toString());
        //remove file from s3 and db
        fileService.deleteFileByUrl(fileUrl);
        this.lessonRepository.save(lesson);
        log.info("File removed from lesson with id: {}", lesson.getId());
        return lesson.getId();
    }

    @Override
    public Long deleteLesson(Long id) {
        LessonEntity lesson = this.findById(id);
        log.info("Delete lesson with id: {}", lesson.getId());
        lesson.setDeleted(true);
        return this.lessonRepository.save(lesson).getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long permanentlyDeleteLesson(Long id) {
        LessonEntity lesson = this.findById(id);
        log.info("Permanently delete lesson with id: {}", lesson.getId());
        // Delete the lesson from the database
        this.lessonRepository.delete(lesson);
        // Delete the video file if it exists
        if (lesson.getVideoUrl() != null) {
            fileService.deleteFileByUrl(lesson.getVideoUrl());
        }
        // Delete the files associated with the lesson
        String fileUrls = lesson.getFileUrl();
        if (fileUrls != null) {
            String[] urls = fileUrls.split(",");
            for (String url : urls) {
                fileService.deleteFileByUrl(url);
            }
        }
        // Delete the lesson from the database
        this.lessonRepository.delete(lesson);
        log.info("Lesson permanently deleted with id: {}", lesson.getId());
        return lesson.getId();
    }

    @Override
    public Long restoreLesson(Long id) {
        LessonEntity lesson = this.findById(id);
        log.info("Restore lesson with id: {}", lesson.getId());
        lesson.setDeleted(false);
        this.lessonRepository.save(lesson);
        log.info("Lesson restored with id: {}", lesson.getId());
        return lesson.getId();
    }
}
