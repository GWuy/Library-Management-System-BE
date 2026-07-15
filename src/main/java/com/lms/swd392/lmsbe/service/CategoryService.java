package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.entity.Category;
import com.lms.swd392.lmsbe.model.request.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategoryById(Integer id);

    Category createCategory(CreateCategoryRequest createCategoryRequest);

    Category updateCategory(Integer id, CreateCategoryRequest createCategoryRequest);

    void deleteCategory(Integer id);
}
