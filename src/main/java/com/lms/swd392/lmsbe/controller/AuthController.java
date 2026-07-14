package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.mapper.UserMapper;
import com.lms.swd392.lmsbe.model.request.RegisterRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.RegisterResponse;
import com.lms.swd392.lmsbe.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    UserService userService;
    UserMapper userMapper;

    @PostMapping(value = "/register", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @RequestPart("data") @Valid RegisterRequest request,
            @RequestPart("avatar") MultipartFile avatar
    ) {
        User user = userService.register(request, avatar);
        RegisterResponse response = userMapper.toRegisterResponse(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Register successfully", response));
    }
}
