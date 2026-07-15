package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.Category;
import com.lms.swd392.lmsbe.mapper.CategoryMapper;
import com.lms.swd392.lmsbe.model.request.CreateCategoryRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.CategoryResponse;
import com.lms.swd392.lmsbe.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get all categories successfully", categoryResponses));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Get category successfully", categoryMapper.toCategoryResponse(category)));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category createdCategory = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Category created successfully", categoryMapper.toCategoryResponse(createdCategory)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Integer id, @Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category updatedCategory = categoryService.updateCategory(id, createCategoryRequest);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", categoryMapper.toCategoryResponse(updatedCategory)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", ""));
    }
}
