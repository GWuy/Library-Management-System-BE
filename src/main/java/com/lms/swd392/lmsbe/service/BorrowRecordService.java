package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.model.response.BorrowRecordResponse;

import java.util.List;

public interface BorrowRecordService {
    BorrowRecordResponse searchActiveBorrowRecord(Integer borrowerId, Integer bookId);
    void returnBook(Integer borrowId);
    List<BorrowRecordResponse> getCurrentBorrowers();
    void processOverdueRecords();
}
