package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.BorrowRecordResponse;
import com.lms.swd392.lmsbe.service.BorrowRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow-records")
@RequiredArgsConstructor
@Tag(name = "Borrow Record", description = "Các API quản lý bản ghi mượn/trả sách")
public class BorrowRecordController {

    private final BorrowRecordService borrowRecordService;

    @Operation(summary = "Tìm kiếm bản ghi mượn đang hoạt động", description = "Tìm bản ghi mượn sách chưa trả dựa trên ID người mượn và ID sách")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<BorrowRecordResponse>> search(
            @Parameter(description = "ID người mượn") @RequestParam Integer borrowerId,
            @Parameter(description = "ID sách") @RequestParam Integer bookId) {
        BorrowRecordResponse response = borrowRecordService.searchActiveBorrowRecord(borrowerId, bookId);
        return ResponseEntity.ok(ApiResponse.success("Borrow record found.", response));
    }

    @Operation(summary = "Trả sách", description = "Cập nhật bản ghi mượn sách thành đã trả")
    @PostMapping("/{borrowId}/return")
    public ResponseEntity<ApiResponse<Void>> returnBook(@PathVariable Integer borrowId) {
        borrowRecordService.returnBook(borrowId);
        return ResponseEntity.ok(ApiResponse.success("Book returned successfully.", null));
    }

    @Operation(summary = "Lấy danh sách người đang mượn sách", description = "Trả về danh sách tất cả các bản ghi mượn sách chưa được trả")
    @GetMapping("/current")
    public ResponseEntity<ApiResponse<List<BorrowRecordResponse>>> getCurrentBorrowers() {
        return ResponseEntity.ok(ApiResponse.success("Get current borrowers successfully", 
                borrowRecordService.getCurrentBorrowers()));
    }
}
