package vn.edu.hcmuaf.fit.elearning.feature.courses.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.CourseEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.ModuleEntity;
import vn.edu.hcmuaf.fit.elearning.feature.courses.repository.ModuleRepository;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.CourseService;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.ModuleService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ModuleServiceImpl  implements ModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseService courseService;

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
}
