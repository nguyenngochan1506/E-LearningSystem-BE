package vn.edu.hcmuaf.fit.elearning.feature.courses.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.UpdateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.ModuleService;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
@Tag(name = "MODULE", description = "Module API")
public class ModuleController {
    private final ModuleService moduleService;
    @PostMapping()
    public ResponseDto createModule(@RequestBody @Valid CreateModuleRequestDto req) {
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("module.create.success"))
                .data(moduleService.createModule(req))
                .build();
    }
    @PutMapping()
    public ResponseDto updateModule(@RequestBody @Valid UpdateModuleRequestDto req) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("module.update.success"))
                .data(moduleService.updateModule(req))
                .build();
    }
}
