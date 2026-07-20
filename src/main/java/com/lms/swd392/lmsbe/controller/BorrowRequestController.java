package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.BorrowRequest;
import com.lms.swd392.lmsbe.exception.BadRequestException;
import com.lms.swd392.lmsbe.mapper.BorrowRequestMapper;
import com.lms.swd392.lmsbe.model.request.ApproveBorrowRequest;
import com.lms.swd392.lmsbe.model.request.CreateBorrowRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.BorrowRequestResponse;
import com.lms.swd392.lmsbe.service.BorrowRequestService;
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
public class BorrowRequestController {

    BorrowRequestService borrowRequestService;
    BorrowRequestMapper borrowRequestMapper;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BorrowRequestResponse>>> getAllRequests() {
        List<BorrowRequest> requests = borrowRequestService.getAllRequests();
        List<BorrowRequestResponse> responses = requests.stream()
                .map(borrowRequestMapper::toBorrowRequestResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get all borrow requests successfully", responses));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<BorrowRequestResponse>>> getPendingRequests() {
        List<BorrowRequest> requests = borrowRequestService.getPendingRequests();
        List<BorrowRequestResponse> responses = requests.stream()
                .map(borrowRequestMapper::toBorrowRequestResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get pending borrow requests successfully", responses));
    }

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

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<ApiResponse<Void>> approveRequest(
            @PathVariable Integer requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        borrowRequestService.approveRequest(requestId, username);
        return ResponseEntity.ok(ApiResponse.success("Borrow request approved successfully.", null));
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectRequest(
            @PathVariable Integer requestId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        borrowRequestService.rejectRequest(requestId, username);
        return ResponseEntity.ok(ApiResponse.success("Borrow request rejected successfully.", null));
    }

    @PutMapping("/{id}/approve")
    @Deprecated
    public ResponseEntity<ApiResponse<BorrowRequestResponse>> approveRequestDeprecated(
            @PathVariable Integer id,
            @Valid @RequestBody ApproveBorrowRequest request) {
        // Keeping this for backward compatibility if needed, but updated to use new service logic or just throw error
        throw new BadRequestException("This endpoint is deprecated. Use POST /api/borrow-requests/{requestId}/approve instead.");
    }

    @PutMapping("/{id}/reject")
    @Deprecated
    public ResponseEntity<ApiResponse<BorrowRequestResponse>> rejectRequestDeprecated(
            @PathVariable Integer id) {
        throw new BadRequestException("This endpoint is deprecated. Use POST /api/borrow-requests/{requestId}/reject instead.");
    }
}
