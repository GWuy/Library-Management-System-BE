package com.lms.swd392.lmsbe.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class BorrowRequestResponse {
    private Integer id;
    private String bookTitle;
    private Integer bookId;
    private String borrowerName;
    private Integer borrowerId;
    private String staffName;
    private Integer staffId;
    private Instant requestDate;
    private Instant responseDate;
    private String status;
    private String note;
}
