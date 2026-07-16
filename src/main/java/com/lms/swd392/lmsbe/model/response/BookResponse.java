package com.lms.swd392.lmsbe.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookResponse {
    private Integer id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private Integer publishedYear;
    private Integer quantity;
    private Integer availableQuantity;
    private String status;
    private String description;
    private List<CategoryResponse> categories;
}
