package vn.edu.ngochandev.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.ngochandev.dto.response.UserResponse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller")
public class UserController {
    @Operation(summary = "Mockup API for User", description = "Mo ta chi tiet")
    @GetMapping()
    public ResponseEntity<?> getUser(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Map<String, Object> res = new HashMap<>();
        List<UserResponse> list = List.of(UserResponse.builder()
                .id(1L)
                .dateOfBirth(LocalDate.of(2004,06,15))
                .email("123@gmail.com")
                .fullName("Nguyen Ngoc Han")
                .gender("Male")
                .phone("123456789")
                .userName("123")
                .userStatus("ACTIVE")
                .build());
        res.put("data", list);
        res.put("message", "OK");
        res.put("status", HttpStatus.OK.value());

        return new ResponseEntity<>(res, HttpStatus.OK);

    }
}
