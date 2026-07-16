package com.lms.swd392.lmsbe.repository.specification;

import com.lms.swd392.lmsbe.constant.BookStatus;
import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.entity.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class BookSpecification {

    private BookSpecification() {
    }

    public static Specification<Book> withFilters(
            String title,
            String author,
            Integer fromYear,
            Integer toYear,
            Set<Integer> categoryIds) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Status always = AVAILABLE
            predicates.add(cb.equal(root.get("status"), BookStatus.AVAILABLE.getValue()));

            // Filter by title (LIKE, case-insensitive)
            if (title != null && !title.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("title")),
                        "%" + title.toLowerCase().trim() + "%"));
            }

            // Filter by author (LIKE, case-insensitive)
            if (author != null && !author.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("author")),
                        "%" + author.toLowerCase().trim() + "%"));
            }

            // Filter by published year range
            if (fromYear != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("publishedYear"), fromYear));
            }
            if (toYear != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("publishedYear"), toYear));
            }

            // Filter by multiple categories (book must belong to at least one of the selected categories)
            if (categoryIds != null && !categoryIds.isEmpty()) {
                Join<Book, Category> categoryJoin = root.join("categories", JoinType.INNER);
                predicates.add(categoryJoin.get("id").in(categoryIds));
                query.distinct(true);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
