package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.constant.BorrowRecordStatus;
import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.entity.BorrowRecord;
import com.lms.swd392.lmsbe.exception.ResourceNotFoundException;
import com.lms.swd392.lmsbe.model.response.BorrowRecordResponse;
import com.lms.swd392.lmsbe.repository.BookRepository;
import com.lms.swd392.lmsbe.repository.BorrowRecordRepository;
import com.lms.swd392.lmsbe.service.BorrowRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;

    @Override
    public BorrowRecordResponse searchActiveBorrowRecord(Integer borrowerId, Integer bookId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findByBorrower_IdAndBook_IdAndStatus(
                borrowerId, bookId, BorrowRecordStatus.BORROWING.getValue()
        ).orElseThrow(() -> new ResourceNotFoundException("Borrow record not found or already returned."));

        return mapToResponse(borrowRecord);
    }

    @Override
    @Transactional
    public void returnBook(Integer borrowId) {
        BorrowRecord borrowRecord = borrowRecordRepository.findById(borrowId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found with id: " + borrowId));

        if (!BorrowRecordStatus.BORROWING.getValue().equals(borrowRecord.getStatus())) {
            throw new ResourceNotFoundException("Borrow record not found or already returned.");
        }

        // Update BorrowRecord
        borrowRecord.setReturnDate(Instant.now());
        borrowRecord.setStatus(BorrowRecordStatus.RETURNED.getValue());
        borrowRecordRepository.save(borrowRecord);

        // Update Book quantity
        Book book = borrowRecord.getBook();
        book.setAvailableQuantity(book.getAvailableQuantity() + 1);
        bookRepository.save(book);
    }

    private BorrowRecordResponse mapToResponse(BorrowRecord borrowRecord) {
        return BorrowRecordResponse.builder()
                .id(borrowRecord.getId())
                .borrowerId(borrowRecord.getBorrower() != null ? borrowRecord.getBorrower().getId() : null)
                .borrowerName(borrowRecord.getBorrower() != null ? borrowRecord.getBorrower().getFullName() : null)
                .bookId(borrowRecord.getBook() != null ? borrowRecord.getBook().getId() : null)
                .bookTitle(borrowRecord.getBook() != null ? borrowRecord.getBook().getTitle() : null)
                .borrowDate(borrowRecord.getBorrowDate())
                .dueDate(borrowRecord.getDueDate())
                .returnDate(borrowRecord.getReturnDate())
                .status(borrowRecord.getStatus())
                .build();
    }
}
