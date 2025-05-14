package vn.edu.hcmuaf.fit.elearning.feature.auth.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.AssignRoleToPermissionRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.AssignRoleToUserRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.RoleCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.dto.req.RoleUpdateRequest;
import vn.edu.hcmuaf.fit.elearning.feature.auth.service.RoleService;
import vn.edu.hcmuaf.fit.elearning.feature.users.UserService;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
@Tag(name = "ROLE", description = "Role API")
public class RoleController {
    private final RoleService roleService;
    private final UserService userService;


    @PostMapping("/assign-permission")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto assignPermissionToRole(@Valid @RequestBody AssignRoleToPermissionRequest req) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.assign.success"))
                .data(roleService.assignRolePermission(req))
                .build();
    }

    @PostMapping("/assign-user")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto assignRoleToUser(@Valid @RequestBody AssignRoleToUserRequest req) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.assign.success"))
                .data(userService.assignRoleToUser(req))
                .build();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createRole(@Valid @RequestBody RoleCreationRequest request) {
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("role.create.success"))
                .data(roleService.createRole(request))
                .build();
    }
    @PutMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto updateRole(@Valid @RequestBody RoleUpdateRequest request) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.update.success"))
                .data(roleService.updateRole(request))
                .build();
    }
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getRoleById(@PathVariable long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.get.success"))
                .data(roleService.getRoleById(id))
                .build();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getAllRoles(@RequestParam(defaultValue = "0") int pageNo,
                                   @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.get.all.success"))
                .data(roleService.getAllRoles(pageNo, pageSize, false))
                .build();
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deleteRole(@PathVariable long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.delete.success"))
                .data(roleService.deleteRole(id))
                .build();
    }

    @GetMapping("/trash")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getAllDeletedRoles(@RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.get.all.trash.success"))
                .data(roleService.getAllRoles(pageNo, pageSize, true))
                .build();
    }
    @PatchMapping("/restore/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto restoreRole(@PathVariable long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("role.restore.success"))
                .data(roleService.restoreRole(id))
                .build();
    }
}
