package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.LostBookReport;
import com.lms.swd392.lmsbe.mapper.LostBookReportMapper;
import com.lms.swd392.lmsbe.model.request.ReportLostBookRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.LostBookReportResponse;
import com.lms.swd392.lmsbe.service.LostBookReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lost-book-reports")
@RequiredArgsConstructor
public class LostBookReportController {

    private final LostBookReportService lostBookReportService;
    private final LostBookReportMapper lostBookReportMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> reportLostBook(@Valid @RequestBody ReportLostBookRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        lostBookReportService.reportLostBook(request, username);
        return ResponseEntity.ok(ApiResponse.success("Lost book report created successfully.", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LostBookReportResponse>>> getAllReports() {
        List<LostBookReport> reports = lostBookReportService.getAllReports();
        List<LostBookReportResponse> responses = reports.stream()
                .map(lostBookReportMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get all lost book reports successfully", responses));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Integer id,
            @RequestParam String status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String staffUsername = authentication.getName();
        lostBookReportService.updateReportStatus(id, status, staffUsername);
        return ResponseEntity.ok(ApiResponse.success("Lost book report status updated successfully", null));
    }
}
