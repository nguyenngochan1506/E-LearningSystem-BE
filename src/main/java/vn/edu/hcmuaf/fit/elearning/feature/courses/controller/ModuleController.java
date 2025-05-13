package vn.edu.hcmuaf.fit.elearning.feature.courses.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.courses.dto.req.CreateModuleRequestDto;
import vn.edu.hcmuaf.fit.elearning.feature.courses.service.ModuleService;

@RestController
@RequestMapping("/api/v1/modules")
@RequiredArgsConstructor
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
}
