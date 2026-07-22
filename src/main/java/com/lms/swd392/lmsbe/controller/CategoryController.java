package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.Category;
import com.lms.swd392.lmsbe.mapper.CategoryMapper;
import com.lms.swd392.lmsbe.model.request.CreateCategoryRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.CategoryResponse;
import com.lms.swd392.lmsbe.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Category", description = "Các API quản lý danh mục sách")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Operation(summary = "Lấy danh sách tất cả danh mục", description = "Trả về danh sách đầy đủ các danh mục sách có trong hệ thống")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get all categories successfully", categoryResponses));
    }

    @Operation(summary = "Xem chi tiết danh mục", description = "Lấy thông tin của một danh mục theo ID")
    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Integer id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(ApiResponse.success("Get category successfully", categoryMapper.toCategoryResponse(category)));
    }

    @Operation(summary = "Thêm danh mục mới", description = "Tạo một danh mục sách mới")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category createdCategory = categoryService.createCategory(createCategoryRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Category created successfully", categoryMapper.toCategoryResponse(createdCategory)));
    }

    @Operation(summary = "Cập nhật danh mục", description = "Cập nhật tên hoặc thông tin danh mục hiện có")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Integer id, @Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
        Category updatedCategory = categoryService.updateCategory(id, createCategoryRequest);
        return ResponseEntity.ok(ApiResponse.success("Category updated successfully", categoryMapper.toCategoryResponse(updatedCategory)));
    }

    @Operation(summary = "Xóa danh mục", description = "Xóa danh mục khỏi hệ thống theo ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("Category deleted successfully", ""));
    }
}
