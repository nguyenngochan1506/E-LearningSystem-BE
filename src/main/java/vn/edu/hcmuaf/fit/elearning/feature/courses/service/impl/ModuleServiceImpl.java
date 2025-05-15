package vn.edu.hcmuaf.fit.elearning.feature.courses.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.LessonResponseDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.res.ModuleResponseDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CourseEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.ModuleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.repository.ModuleRepository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CourseService;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.ModuleService;
import vn.edu.hcmuaf.fit.elearning.feature.file.FileService;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModuleServiceImpl  implements ModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseService courseService;
    private final FileService fileService;

    @Override
    public Long createModule(CreateModuleRequestDto req) {
        log.info("Create module with name: {}", req.getName());
        //check if course exists
        CourseEntity course = courseService.findById(req.getCourseId());

        ModuleEntity module = new ModuleEntity();
        module.setName(req.getName());
        module.setDescription(req.getDescription());
        module.setNumber(req.getNumber());
        module.setCourse(course);
        ModuleEntity savedModule = moduleRepository.save(module);
        log.info("Module created with id: {}", savedModule.getId());
        return savedModule.getId();
    }

    @Override
    public ModuleEntity findById(Long id) {
        return moduleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Translator.translate("module.not.found")));
    }

    @Override
    public Long updateModule(UpdateModuleRequestDto req) {
        ModuleEntity module = this.findById(req.getId());
        log.info("Update module with id: {}", module.getId());
        //check if course exists
        module.setName(req.getName() != null ? req.getName() : module.getName());
        module.setDescription(req.getDescription() != null ? req.getDescription() : module.getDescription());
        module.setNumber(req.getNumber() != null ? req.getNumber() : module.getNumber());
        ModuleEntity savedModule = moduleRepository.save(module);
        log.info("Module updated with id: {}", savedModule.getId());
        return savedModule.getId();
    }

    @Override
    public Long deleteModule(Long id) {
        ModuleEntity module = this.findById(id);
        log.info("Delete module with id: {}", module.getId());
        module.setDeleted(true);
        if(module.getLessons() != null && !module.getLessons().isEmpty()) {
            module.getLessons().forEach(lesson -> lesson.setDeleted(true));
        }
        moduleRepository.save(module);
        log.info("Module deleted with id: {}", module.getId());

        return module.getId();
    }

    @Override
    public Long restoreModule(Long id) {
        ModuleEntity module = this.findById(id);
        log.info("Restore module with id: {}", module.getId());
        module.setDeleted(false);
        if(module.getLessons() != null && !module.getLessons().isEmpty()) {
            module.getLessons().forEach(lesson -> lesson.setDeleted(false));
        }
        moduleRepository.save(module);
        log.info("Module restored with id: {}", module.getId());
        return module.getId();
    }

    @Override
    public Long permanentlyDeleteModule(Long id) {
        ModuleEntity module = this.findById(id);
        log.info("Permanently delete module with id: {}", module.getId());
        //delete file related to module
        if(module.getLessons() != null && !module.getLessons().isEmpty()) {
            module.getLessons().forEach(lesson -> {
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
            });
        }
        moduleRepository.delete(module);
        log.info("Module permanently deleted with id: {}", module.getId());
        return module.getId();
    }

    @Override
    public ModuleResponseDto getModule(Long id) {
        ModuleEntity module = this.findById(id);
        log.info("Get module with id: {}", module.getId());
        return convertToResponseDto(module);
    }

    private ModuleResponseDto convertToResponseDto(ModuleEntity module) {
        List<LessonResponseDto> lessons = new ArrayList<>();
        if (module.getLessons() != null && !module.getLessons().isEmpty()) {
            module.getLessons().forEach(lesson -> {
                lessons.add(LessonResponseDto.builder()
                        .id(lesson.getId())
                        .name(lesson.getName())
                        .content(lesson.getContent())
                        .videoUrl(lesson.getVideoUrl())
                        .files(List.of())
                        .createdAt(lesson.getCreatedAt())
                        .updatedAt(lesson.getUpdatedAt())
                        .createdBy(lesson.getCreatedBy())
                        .updatedBy(lesson.getUpdatedBy())
                        .build());
            });
        }

         // Convert module to ModuleResponseDto

        return ModuleResponseDto.builder()
                .id(module.getId())
                .name(module.getName())
                .description(module.getDescription())
                .number(module.getNumber())
                .lessons(lessons)
                .createdAt(module.getCreatedAt())
                .updatedAt(module.getUpdatedAt())
                .createdBy(module.getCreatedBy())
                .updatedBy(module.getUpdatedBy())
                .build();
    }
}
