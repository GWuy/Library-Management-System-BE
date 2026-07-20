package com.lms.swd392.lmsbe.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class BorrowRecordResponse {
    private Integer id;
    private Integer borrowerId;
    private String borrowerName;
    private Integer bookId;
    private String bookTitle;
    private Instant borrowDate;
    private Instant dueDate;
    private Instant returnDate;
    private String status;
}
