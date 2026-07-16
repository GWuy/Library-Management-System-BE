package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.model.request.CreateBookRequest;
import com.lms.swd392.lmsbe.model.request.UpdateBookRequest;

import java.util.List;
import java.util.Set;

public interface BookService {

    List<Book> getAllBooks(String title, String author, Integer fromYear, Integer toYear, Set<Integer> categoryIds);

    Book getBookById(Integer id);

    Book createBook(CreateBookRequest request);

    Book updateBook(Integer id, UpdateBookRequest request);

    void deleteBook(Integer id);
}
