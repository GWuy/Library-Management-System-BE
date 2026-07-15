package com.lms.swd392.lmsbe.repository;

import com.lms.swd392.lmsbe.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
