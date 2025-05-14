package vn.edu.hcmuaf.fit.elearning.feature.users;


import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.elearning.common.ResponseDto;
import vn.edu.hcmuaf.fit.elearning.common.Translator;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.req.UserChangePasswordRequest;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.req.UserCreationRequest;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.req.UserUpdateInfoRequest;
import vn.edu.hcmuaf.fit.elearning.feature.users.dto.res.UserPageResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User", description = "User API")
public class UserController {
    private final UserService userService;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getUser(@PathVariable("id") Long id){
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.get.success"))
                .data(userService.getUserById(id))
                .build();
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getAllUsers( @RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) String sort,
                                    @RequestParam(defaultValue = "0") int pageNo,
                                    @RequestParam(defaultValue = "10") int pageSize){
        UserPageResponse users = userService.getAllUsers(keyword, sort, pageNo, pageSize, false);
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.get.all.success"))
                .data(users)
                .build();
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseDto createUser(@Valid @RequestBody UserCreationRequest req){
        return ResponseDto.builder()
                .status(HttpStatus.CREATED.value())
                .message(Translator.translate("user.create.success"))
                .data(userService.createUser(req))
                .build();
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseDto updateUser( @Valid @RequestBody  UserUpdateInfoRequest userUpdateRequest) {
        return ResponseDto.builder()
                .status(HttpStatus.ACCEPTED.value())
                .message(Translator.translate("user.update.success"))
                .data(userService.updateUserInfoById(userUpdateRequest))
                .build();
    }

    @PatchMapping("/change-pwd")
    public ResponseDto changePassword(@Valid @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.change-password.success"))
                .data(userService.changePassword(userChangePasswordRequest))
                .build();
    }
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto deleteUser(@PathVariable("id") Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.delete.success"))
                .data(userService.deleteUserById(id))
                .build();
    }
    @GetMapping("/trash")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto getAllDeletedUsers(@RequestParam(required = false) String keyword,
                                          @RequestParam(required = false) String sort,
                                          @RequestParam(defaultValue = "0") int pageNo,
                                          @RequestParam(defaultValue = "10") int pageSize){
        UserPageResponse users = userService.getAllUsers(keyword, sort, pageNo, pageSize, true);
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.get.trash.all.success"))
                .data(users)
                .build();
    }
    @PatchMapping("/restore/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseDto restoreUser(@PathVariable("id") Long id) {
        return ResponseDto.builder()
                .status(HttpStatus.OK.value())
                .message(Translator.translate("user.restore.success"))
                .data(userService.restoreUserById(id))
                .build();
    }
}
