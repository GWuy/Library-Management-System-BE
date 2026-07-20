package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.model.request.ReportLostBookRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.service.LostBookReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/lost-book-reports")
@RequiredArgsConstructor
public class LostBookReportController {

    private final LostBookReportService lostBookReportService;

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> reportLostBook(@Valid @RequestBody ReportLostBookRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        lostBookReportService.reportLostBook(request, username);
        return ResponseEntity.ok(ApiResponse.success("Lost book report created successfully.", null));
    }
}
