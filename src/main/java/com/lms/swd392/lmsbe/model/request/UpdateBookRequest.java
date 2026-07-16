package com.lms.swd392.lmsbe.model.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateBookRequest {

    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must not exceed 255 characters")
    private String title;

    @Size(max = 255, message = "Author must not exceed 255 characters")
    private String author;

    @Size(max = 255, message = "Publisher must not exceed 255 characters")
    private String publisher;

    @Size(max = 50, message = "ISBN must not exceed 50 characters")
    private String isbn;

    private Integer publishedYear;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    private String description;

    @NotEmpty(message = "At least one category is required")
    private Set<Integer> categoryIds;
}
