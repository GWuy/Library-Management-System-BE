package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.constant.BorrowRecordStatus;
import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.entity.BorrowRecord;
import com.lms.swd392.lmsbe.exception.ResourceNotFoundException;
import com.lms.swd392.lmsbe.model.response.BorrowRecordResponse;
import com.lms.swd392.lmsbe.repository.BookRepository;
import com.lms.swd392.lmsbe.repository.BorrowRecordRepository;
import com.lms.swd392.lmsbe.service.BorrowRecordService;
import com.lms.swd392.lmsbe.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BorrowRecordServiceImpl implements BorrowRecordService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final EmailService emailService;

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

        if (!BorrowRecordStatus.BORROWING.getValue().equals(borrowRecord.getStatus()) &&
            !BorrowRecordStatus.OVERDUE.getValue().equals(borrowRecord.getStatus())) {
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

    @Override
    public List<BorrowRecordResponse> getCurrentBorrowers() {
        return borrowRecordRepository.findByStatusIn(List.of(
                BorrowRecordStatus.BORROWING.getValue(),
                BorrowRecordStatus.OVERDUE.getValue()
        )).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void processOverdueRecords() {
        List<BorrowRecord> activeRecords = borrowRecordRepository.findByStatusIn(List.of(
                BorrowRecordStatus.BORROWING.getValue(),
                BorrowRecordStatus.OVERDUE.getValue()
        ));

        Instant now = Instant.now();
        Instant fiveDaysFromNow = now.plus(5, ChronoUnit.DAYS);

        for (BorrowRecord record : activeRecords) {
            String borrowerEmail = record.getBorrower().getEmail();
            String bookTitle = record.getBook().getTitle();

            if (record.getStatus().equals(BorrowRecordStatus.BORROWING.getValue())) {
                if (record.getDueDate().isBefore(now)) {
                    // Mark as OVERDUE
                    record.setStatus(BorrowRecordStatus.OVERDUE.getValue());
                    borrowRecordRepository.save(record);
                    
                    // Send Overdue Email
                    emailService.sendSimpleMessage(
                            borrowerEmail,
                            "Overdue Book Return Notice",
                            "Dear " + record.getBorrower().getFullName() + ",\n\n" +
                            "The book '" + bookTitle + "' was due on " + record.getDueDate() + ".\n" +
                            "Your record has been marked as OVERDUE. Please return it as soon as possible."
                    );
                } else if (record.getDueDate().isBefore(fiveDaysFromNow)) {
                    // Send Reminder Email
                    emailService.sendSimpleMessage(
                            borrowerEmail,
                            "Reminder: Book Return Due Soon",
                            "Dear " + record.getBorrower().getFullName() + ",\n\n" +
                            "The book '" + bookTitle + "' is due on " + record.getDueDate() + ".\n" +
                            "Please return it on time."
                    );
                }
            } else if (record.getStatus().equals(BorrowRecordStatus.OVERDUE.getValue())) {
                // Already overdue, send another notice
                emailService.sendSimpleMessage(
                        borrowerEmail,
                        "CRITICAL: Overdue Book Return Notice",
                        "Dear " + record.getBorrower().getFullName() + ",\n\n" +
                        "The book '" + bookTitle + "' is still OVERDUE (Due date: " + record.getDueDate() + ").\n" +
                        "Please return it immediately to avoid further penalties."
                );
            }
        }
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
