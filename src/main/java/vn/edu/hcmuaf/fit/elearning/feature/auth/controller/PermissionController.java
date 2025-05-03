package vn.edu.hcmuaf.fit.elearning.feature.auth.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.PermissionCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.PermissionUpdateRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.PermissionService;

@RestController
@RequestMapping("/api/v1/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createPermission(@Valid @RequestBody PermissionCreationRequest request) {
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("permission.create.success"))
                .data(permissionService.createPermission(request))
                .build();
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto updatePermission(@Valid @RequestBody PermissionUpdateRequest request) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("permission.update.success"))
                .data(permissionService.updatePermission(request))
                .build();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getPermissionById(@PathVariable long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("permission.get.success"))
                .data(permissionService.getPermissionById(id))
                .build();
    }
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getAllPermissions(@RequestParam(defaultValue = "1") int pageNo,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("permission.get.all.success"))
                .data(permissionService.getAllPermissions(pageNo, pageSize, false))
                .build();
    }
    @GetMapping("/trash")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getAllDeletedPermissions(@RequestParam(defaultValue = "1") int pageNo,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("permission.get.all.trash.success"))
                .data(permissionService.getAllPermissions(pageNo, pageSize, true))
                .build();
    }
    @PatchMapping("/restore/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto restorePermission(@PathVariable long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("permission.restore.success"))
                .data(permissionService.restorePermission(id))
                .build();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deletePermission(@PathVariable long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("permission.delete.success"))
                .data(permissionService.deletePermission(id))
                .build();
    }
}
