package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.constant.BookStatus;
import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.entity.Category;
import com.lms.swd392.lmsbe.exception.BadRequestException;
import com.lms.swd392.lmsbe.exception.ConflictException;
import com.lms.swd392.lmsbe.exception.ResourceNotFoundException;
import com.lms.swd392.lmsbe.model.request.CreateBookRequest;
import com.lms.swd392.lmsbe.model.request.UpdateBookRequest;
import com.lms.swd392.lmsbe.repository.BookRepository;
import com.lms.swd392.lmsbe.repository.CategoryRepository;
import com.lms.swd392.lmsbe.repository.specification.BookSpecification;
import com.lms.swd392.lmsbe.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<Book> getAllBooks(String title, String author, Integer fromYear, Integer toYear, Set<Integer> categoryIds) {
        Specification<Book> spec = BookSpecification.withFilters(title, author, fromYear, toYear, categoryIds);
        return bookRepository.findAll(spec);
    }

    @Override
    public Book getBookById(Integer id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found."));
    }

    @Override
    @Transactional
    public Book createBook(CreateBookRequest request) {
        // Validate ISBN uniqueness if provided
        if (request.getIsbn() != null && !request.getIsbn().isBlank()) {
            if (bookRepository.existsByIsbn(request.getIsbn())) {
                throw new ConflictException("A book with this ISBN already exists.");
            }
        }

        if (request.getPublishedYear() < 0) {
            throw new BadRequestException("Published year cannot be negative.");
        }

        if (request.getPublishedYear() > LocalDateTime.now().getYear()) {
            throw new BadRequestException("Published year cannot be in the future.");
        }

        // Validate and fetch categories
        Set<Category> categories = resolveCategories(request.getCategoryIds());

        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setQuantity(request.getQuantity());
        book.setAvailableQuantity(request.getQuantity());
        book.setStatus(BookStatus.AVAILABLE.getValue());
        book.setDescription(request.getDescription());
        book.setCategories(categories);

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(Integer id, UpdateBookRequest request) {
        Book book = getBookById(id);

        // Validate ISBN uniqueness if provided (exclude current book)
        if (request.getIsbn() != null && !request.getIsbn().isBlank()) {
            if (bookRepository.existsByIsbnAndIdNot(request.getIsbn(), id)) {
                throw new ConflictException("A book with this ISBN already exists.");
            }
        }

        // Validate and fetch categories
        Set<Category> categories = resolveCategories(request.getCategoryIds());

        // Calculate new available quantity based on quantity change
        int quantityDiff = request.getQuantity() - book.getQuantity();
        int newAvailable = book.getAvailableQuantity() + quantityDiff;
        if (newAvailable < 0) {
            throw new BadRequestException("New quantity cannot be less than the number of currently borrowed books.");
        }

        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPublisher(request.getPublisher());
        book.setIsbn(request.getIsbn());
        book.setPublishedYear(request.getPublishedYear());
        book.setQuantity(request.getQuantity());
        book.setAvailableQuantity(newAvailable);
        book.setDescription(request.getDescription());
        book.setCategories(categories);

        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public void deleteBook(Integer id) {
        Book book = getBookById(id);
        bookRepository.delete(book);
    }

    /**
     * Resolves a set of category IDs into Category entities.
     * Throws ResourceNotFoundException if any category ID does not exist.
     */
    private Set<Category> resolveCategories(Set<Integer> categoryIds) {
        Set<Category> categories = new LinkedHashSet<>();
        for (Integer categoryId : categoryIds) {
            Category category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Category not found with id: " + categoryId));
            categories.add(category);
        }
        return categories;
    }
}
