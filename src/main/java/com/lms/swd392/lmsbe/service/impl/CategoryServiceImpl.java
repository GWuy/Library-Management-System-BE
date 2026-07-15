package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.entity.Category;
import com.lms.swd392.lmsbe.exception.ConflictException;
import com.lms.swd392.lmsbe.exception.ResourceNotFoundException;
import com.lms.swd392.lmsbe.model.request.CreateCategoryRequest;
import com.lms.swd392.lmsbe.repository.CategoryRepository;
import com.lms.swd392.lmsbe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public Category createCategory(CreateCategoryRequest createCategoryRequest) {
        Category category = new Category();

        if (categoryRepository.existsByCategoryNameIgnoreCase(createCategoryRequest.getName())) {
            throw new ConflictException("This Category existed");
        }

        category.setCategoryName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Integer id, CreateCategoryRequest createCategoryRequest) {
        Category category = getCategoryById(id);

        if (category == null) {
            throw new ResourceNotFoundException("Category not found");
        }
        
        category.setCategoryName(createCategoryRequest.getName());
        category.setDescription(createCategoryRequest.getDescription());
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
}
