package vn.edu.hcmuaf.fit.elearning.feature.courses.service;

import jakarta.validation.Valid;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.entity.ModuleEntity;

public interface ModuleService {
    Long createModule(CreateModuleRequestDto req);
    ModuleEntity findById(Long id);
    Long updateModule( UpdateModuleRequestDto req);
}
