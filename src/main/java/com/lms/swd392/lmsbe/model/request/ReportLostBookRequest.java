package com.lms.swd392.lmsbe.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReportLostBookRequest {

    @NotNull(message = "Borrow ID is required")
    private Integer borrowId;

    @NotBlank(message = "Description is required")
    private String description;
}
