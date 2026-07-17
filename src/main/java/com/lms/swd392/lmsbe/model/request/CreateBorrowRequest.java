package com.lms.swd392.lmsbe.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateBorrowRequest {

    @NotNull(message = "Book ID is required")
    private Integer bookId;

    private String note;
}
