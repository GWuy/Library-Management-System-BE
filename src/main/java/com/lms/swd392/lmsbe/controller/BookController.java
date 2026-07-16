package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.mapper.BookMapper;
import com.lms.swd392.lmsbe.model.request.CreateBookRequest;
import com.lms.swd392.lmsbe.model.request.UpdateBookRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.BookResponse;
import com.lms.swd392.lmsbe.service.BookService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Integer fromYear,
            @RequestParam(required = false) Integer toYear,
            @RequestParam(required = false) Set<Integer> categoryIds) {
        List<Book> books = bookService.getAllBooks(title, author, fromYear, toYear, categoryIds);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get all books successfully", bookResponses));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable Integer id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success("Get book successfully", bookMapper.toBookResponse(book)));
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@Valid @RequestBody CreateBookRequest request) {
        Book createdBook = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book created successfully", bookMapper.toBookResponse(createdBook)));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateBookRequest request) {
        Book updatedBook = bookService.updateBook(id, request);
        return ResponseEntity.ok(ApiResponse.success("Book updated successfully", bookMapper.toBookResponse(updatedBook)));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book deleted successfully", ""));
    }
}
