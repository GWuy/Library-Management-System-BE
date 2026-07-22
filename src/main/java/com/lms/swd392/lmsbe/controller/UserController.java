package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.mapper.UserMapper;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.UserResponse;
import com.lms.swd392.lmsbe.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User", description = "Các API quản lý người dùng")
public class UserController {

    UserService userService;
    UserMapper userMapper;

    @Operation(summary = "Lấy danh sách tất cả người dùng", description = "Trả về danh sách tất cả các tài khoản có trong hệ thống")
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> responses = users.stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get all users successfully", responses));
    }

    @Operation(summary = "Xem thông tin người dùng", description = "Lấy thông tin chi tiết của một người dùng theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Integer id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success("Get user successfully", userMapper.toUserResponse(user)));
    }

    @Operation(summary = "Cập nhật trạng thái người dùng", description = "Thay đổi trạng thái hoạt động của tài khoản (VD: ACTIVE, INACTIVE)")
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Integer id,
            @Parameter(description = "Trạng thái mới") @RequestParam String status) {
        userService.updateStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("User status updated successfully", null));
    }

    @Operation(summary = "Cập nhật vai trò người dùng", description = "Thay đổi vai trò của tài khoản (VD: ADMIN, LIBRARIAN, MEMBER)")
    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<Void>> updateRole(
            @PathVariable Integer id,
            @Parameter(description = "Vai trò mới") @RequestParam String role) {
        userService.updateRole(id, role);
        return ResponseEntity.ok(ApiResponse.success("User role updated successfully", null));
    }
}
