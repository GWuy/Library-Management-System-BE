package com.lms.swd392.lmsbe.model.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class LostBookReportResponse {
    private Integer id;
    private Integer borrowId;
    private String bookTitle;
    private String borrowerName;
    private String description;
    private Instant reportDate;
    private String status;
}
