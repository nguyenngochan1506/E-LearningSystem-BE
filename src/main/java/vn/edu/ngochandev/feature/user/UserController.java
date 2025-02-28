package vn.edu.ngochandev.feature.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.ngochandev.feature.user.dto.request.UserChangePasswordRequest;
import vn.edu.ngochandev.feature.user.dto.request.UserCreationRequest;
import vn.edu.ngochandev.feature.user.dto.request.UserUpdateRequest;
import vn.edu.ngochandev.feature.user.dto.response.UserResponse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller")
public class UserController {
    private final UserService userService;

    @Operation(summary = "Get user", description = "get a user info")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserDetail(@PathVariable long id) {
        return new ResponseEntity<UserResponse>(userService.getUserDetailById(id),HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Delete user", description = "delete a user")
    @DeleteMapping("/del/{id}")
    public ResponseEntity<?> changePassword(@PathVariable long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("delete password",HttpStatus.ACCEPTED);
    }
    @Operation(summary = "change password user", description = "change password for user")
    @PatchMapping("/change-pwd")
    public ResponseEntity<?> changePassword(@RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(userChangePasswordRequest);
        return new ResponseEntity<>("change password",HttpStatus.ACCEPTED);
    }

    @Operation(summary = "Update User", description = "update info a user")
    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
        return new ResponseEntity<>("updated", HttpStatus.ACCEPTED);
    }
    @Operation(summary = "Create User", description = "create a new user")
    @PostMapping()
    public ResponseEntity<Long> createUser(@RequestBody UserCreationRequest req){
        return new ResponseEntity<>(userService.saveUser(req), HttpStatus.CREATED);
    }

    @Operation(summary = "Mockup API for User", description = "Mo ta chi tiet")
    @GetMapping()
    public ResponseEntity<?> getUser(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
//        Map<String, Object> res = new HashMap<>();
//        List<UserResponse> list = List.of(UserResponse.builder()
//                .id(1L)
//                .dateOfBirth(LocalDate.of(2004,06,15))
//                .email("123@gmail.com")
//                .fullName("Nguyen Ngoc Han")
//                .gender("Male")
//                .phoneNumber("123456789")
//                .userName("123")
//                .userStatus("ACTIVE")
//                .build());
//        res.put("data", list);
//        res.put("message", "OK");
//        res.put("status", HttpStatus.OK.value());

        return new ResponseEntity<>(null, HttpStatus.OK);

    }
}
