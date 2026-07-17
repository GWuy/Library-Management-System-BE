package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.BorrowRequest;
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

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse<BorrowRequestResponse>> approveRequest(
            @PathVariable Integer id,
            @Valid @RequestBody ApproveBorrowRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        BorrowRequest borrowRequest = borrowRequestService.approveRequest(id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Borrow request approved successfully",
                borrowRequestMapper.toBorrowRequestResponse(borrowRequest)));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse<BorrowRequestResponse>> rejectRequest(
            @PathVariable Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        BorrowRequest borrowRequest = borrowRequestService.rejectRequest(id, username);
        return ResponseEntity.ok(ApiResponse.success("Borrow request rejected successfully",
                borrowRequestMapper.toBorrowRequestResponse(borrowRequest)));
    }
}
