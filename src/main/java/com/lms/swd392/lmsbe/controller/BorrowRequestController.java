package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.BorrowRequest;
import com.lms.swd392.lmsbe.exception.BadRequestException;
import com.lms.swd392.lmsbe.mapper.BorrowRequestMapper;
import com.lms.swd392.lmsbe.model.request.ApproveBorrowRequest;
import com.lms.swd392.lmsbe.model.request.CreateBorrowRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.BorrowRequestResponse;
import com.lms.swd392.lmsbe.service.BorrowRequestService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/borrow-requests")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Borrow Request", description = "Các API quản lý yêu cầu mượn sách")
public class BorrowRequestController {

    BorrowRequestService borrowRequestService;
    BorrowRequestMapper borrowRequestMapper;

    @Operation(summary = "Lấy tất cả yêu cầu mượn sách", description = "Trả về danh sách tất cả các yêu cầu mượn sách trong hệ thống")
    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowRequestResponse>>> getAllRequests() {
        List<BorrowRequest> requests = borrowRequestService.getAllRequests();
        List<BorrowRequestResponse> responses = requests.stream()
                .map(borrowRequestMapper::toBorrowRequestResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get all borrow requests successfully", responses));
    }

    @Operation(summary = "Lấy các yêu cầu đang chờ duyệt", description = "Trả về danh sách các yêu cầu mượn sách có trạng thái PENDING")
    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<BorrowRequestResponse>>> getPendingRequests() {
        List<BorrowRequest> requests = borrowRequestService.getPendingRequests();
        List<BorrowRequestResponse> responses = requests.stream()
                .map(borrowRequestMapper::toBorrowRequestResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get pending borrow requests successfully", responses));
    }

    @Operation(summary = "Gửi yêu cầu mượn sách", description = "Người dùng gửi yêu cầu mượn một hoặc nhiều cuốn sách")
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<BorrowRequestResponse>> sendRequest(
            @Valid @RequestBody CreateBorrowRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        BorrowRequest borrowRequest = borrowRequestService.sendRequest(request, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Borrow request sent successfully",
                        borrowRequestMapper.toBorrowRequestResponse(borrowRequest)));
    }

    @Operation(summary = "Phê duyệt yêu cầu mượn", description = "Thủ thư phê duyệt yêu cầu mượn sách của người dùng")
    @PostMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveRequest(
            @PathVariable Integer requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        borrowRequestService.approveRequest(requestId, username);
        return ResponseEntity.ok(ApiResponse.success("Borrow request approved successfully.", null));
    }

    @Operation(summary = "Từ chối yêu cầu mượn", description = "Thủ thư từ chối yêu cầu mượn sách của người dùng")
    @PostMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectRequest(
            @PathVariable Integer requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        borrowRequestService.rejectRequest(requestId, username);
        return ResponseEntity.ok(ApiResponse.success("Borrow request rejected successfully.", null));
    }

}
