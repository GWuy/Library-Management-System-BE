package com.lms.swd392.lmsbe.mapper;

import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.entity.Category;
import com.lms.swd392.lmsbe.model.response.BookResponse;
import com.lms.swd392.lmsbe.model.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapCategories")
    BookResponse toBookResponse(Book book);

    @Named("mapCategories")
    default List<CategoryResponse> mapCategories(Set<Category> categories) {
        if (categories == null) {
            return List.of();
        }
        return categories.stream()
                .map(category -> CategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getCategoryName())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
