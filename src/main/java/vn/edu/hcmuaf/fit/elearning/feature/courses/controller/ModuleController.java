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
    @GetMapping("/{id}")
    public ResponseDto getModule(@PathVariable Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("module.get.success"))
                .data(moduleService.getModule(id))
                .build();
    }

    @DeleteMapping("/permanently/{id}")
    public ResponseDto permanentlyDeleteModule(@PathVariable Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("module.permanently.delete.success"))
                .data(moduleService.permanentlyDeleteModule(id))
                .build();
    }

    @PatchMapping("/restore/{id}")
    public ResponseDto restoreModule(@PathVariable Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("module.restore.success"))
                .data(moduleService.restoreModule(id))
                .build();
    }
    @DeleteMapping("{id}")
    public ResponseDto deleteModule(@PathVariable("id") Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("module.delete.success"))
                .data(moduleService.deleteModule(id))
                .build();
    }
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
