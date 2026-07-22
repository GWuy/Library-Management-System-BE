package com.lms.swd392.lmsbe.controller;

import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.mapper.BookMapper;
import com.lms.swd392.lmsbe.model.request.CreateBookRequest;
import com.lms.swd392.lmsbe.model.request.UpdateBookRequest;
import com.lms.swd392.lmsbe.model.response.ApiResponse;
import com.lms.swd392.lmsbe.model.response.BookResponse;
import com.lms.swd392.lmsbe.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Book", description = "Các API quản lý sách trong thư viện")
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @Operation(summary = "Lấy danh sách sách", description = "Tìm kiếm và lọc sách theo tiêu đề, tác giả, năm và thể loại")
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getAllBooks(
            @Parameter(description = "Tiêu đề sách") @RequestParam(required = false) String title,
            @Parameter(description = "Tác giả") @RequestParam(required = false) String author,
            @Parameter(description = "Từ năm") @RequestParam(required = false) Integer fromYear,
            @Parameter(description = "Đến năm") @RequestParam(required = false) Integer toYear,
            @Parameter(description = "Danh sách ID thể loại") @RequestParam(required = false) Set<Integer> categoryIds) {
        List<Book> books = bookService.getAllBooks(title, author, fromYear, toYear, categoryIds);
        List<BookResponse> bookResponses = books.stream()
                .map(bookMapper::toBookResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Get all books successfully", bookResponses));
    }

    @Operation(summary = "Xem chi tiết sách", description = "Lấy thông tin chi tiết của một cuốn sách theo ID")
    @GetMapping("/detail/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookById(@PathVariable Integer id) {
        Book book = bookService.getBookById(id);
        return ResponseEntity.ok(ApiResponse.success("Get book successfully", bookMapper.toBookResponse(book)));
    }

    @Operation(summary = "Thêm sách mới", description = "Tạo một cuốn sách mới trong hệ thống")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@Valid @RequestBody CreateBookRequest request) {
        Book createdBook = bookService.createBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Book created successfully", bookMapper.toBookResponse(createdBook)));
    }

    @Operation(summary = "Cập nhật thông tin sách", description = "Cập nhật các thông tin của cuốn sách hiện có")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateBookRequest request) {
        Book updatedBook = bookService.updateBook(id, request);
        return ResponseEntity.ok(ApiResponse.success("Book updated successfully", bookMapper.toBookResponse(updatedBook)));
    }

    @Operation(summary = "Xóa sách", description = "Xóa một cuốn sách khỏi hệ thống theo ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(ApiResponse.success("Book deleted successfully", ""));
    }
}
