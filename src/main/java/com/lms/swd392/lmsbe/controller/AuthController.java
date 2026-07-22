package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.mapper.UserMapper;
import com.lms.swd392.lmsbe.model.request.LoginRequest;
import com.lms.swd392.lmsbe.model.request.RefreshTokenRequest;
import com.lms.swd392.lmsbe.model.request.RegisterRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.LoginResponse;
import com.lms.swd392.lmsbe.model.response.RegisterResponse;
import com.lms.swd392.lmsbe.service.AuthService;
import com.lms.swd392.lmsbe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Authentication", description = "Các API liên quan đến xác thực và quản lý tài khoản")
public class AuthController {

    UserService userService;
    UserMapper userMapper;
    AuthService authService;

    @Operation(summary = "Đăng ký người dùng mới", description = "Tạo tài khoản mới với thông tin và ảnh đại diện")
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

    @Operation(summary = "Đăng nhập", description = "Xác thực người dùng và trả về JWT token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @RequestBody @Valid LoginRequest request
    ) {
        LoginResponse response = authService.authenticate(request);
        return ResponseEntity.ok(ApiResponse.success("Login successfully", response));
    }

    @Operation(summary = "Làm mới token", description = "Sử dụng Refresh Token để lấy Access Token mới")
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<LoginResponse>> refreshToken(
            @RequestBody @Valid RefreshTokenRequest request
    ) {
        LoginResponse response = authService.refreshToken(request);
        return ResponseEntity.ok(ApiResponse.success("Token refreshed successfully", response));
    }

    @Operation(summary = "Đăng xuất", description = "Hủy phiên làm việc của người dùng")
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        authService.logout(username);
        return ResponseEntity.ok(ApiResponse.success("Logged out successfully", null));
    }
}
