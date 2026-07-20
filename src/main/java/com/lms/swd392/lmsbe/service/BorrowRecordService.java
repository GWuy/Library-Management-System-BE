package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.model.response.BorrowRecordResponse;

public interface BorrowRecordService {
    BorrowRecordResponse searchActiveBorrowRecord(Integer borrowerId, Integer bookId);
    void returnBook(Integer borrowId);
}
