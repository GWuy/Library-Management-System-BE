package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.BorrowRecordResponse;
import com.lms.swd392.lmsbe.service.BorrowRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
@RequiredArgsConstructor
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<BorrowRecordResponse>> search(
            @RequestParam Integer borrowerId,
            @RequestParam Integer bookId) {
        BorrowRecordResponse response = borrowRecordService.searchActiveBorrowRecord(borrowerId, bookId);
        return ResponseEntity.ok(ApiResponse.success("Borrow record found.", response));
    }

    @PostMapping("/{borrowId}/return")
    public ResponseEntity<ApiResponse<Void>> returnBook(@PathVariable Integer borrowId) {
        borrowRecordService.returnBook(borrowId);
        return ResponseEntity.ok(ApiResponse.success("Book returned successfully.", null));
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<List<BorrowRecordResponse>>> getCurrentBorrowers() {
        return ResponseEntity.ok(ApiResponse.success("Get current borrowers successfully", 
                borrowRecordService.getCurrentBorrowers()));
    }
}
