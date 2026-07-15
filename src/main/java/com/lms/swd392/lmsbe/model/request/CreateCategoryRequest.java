package com.lms.swd392.lmsbe.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message = "Category name is required")
    private String name;

    private String description;
}
