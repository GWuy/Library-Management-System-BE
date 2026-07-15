package com.lms.swd392.lmsbe.mapper;

import com.lms.swd392.lmsbe.entity.Category;
import com.lms.swd392.lmsbe.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(source = "categoryName", target = "name")
    CategoryResponse toCategoryResponse(Category category);
}
