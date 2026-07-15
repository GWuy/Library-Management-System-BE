package com.lms.swd392.lmsbe.repository;

import com.lms.swd392.lmsbe.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    boolean existsByCategoryNameIgnoreCase(String categoryName);
}
