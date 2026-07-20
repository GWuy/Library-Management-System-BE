package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.constant.BorrowRecordStatus;
import com.lms.swd392.lmsbe.constant.BorrowRequestStatus;
import com.lms.swd392.lmsbe.constant.BookStatus;
import com.lms.swd392.lmsbe.entity.Book;
import com.lms.swd392.lmsbe.entity.BorrowRecord;
import com.lms.swd392.lmsbe.entity.BorrowRequest;
import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.exception.BadRequestException;
import com.lms.swd392.lmsbe.exception.ResourceNotFoundException;
import com.lms.swd392.lmsbe.model.request.ApproveBorrowRequest;
import com.lms.swd392.lmsbe.model.request.CreateBorrowRequest;
import com.lms.swd392.lmsbe.repository.BookRepository;
import com.lms.swd392.lmsbe.repository.BorrowRecordRepository;
import com.lms.swd392.lmsbe.repository.BorrowRequestRepository;
import com.lms.swd392.lmsbe.repository.UserRepository;
import com.lms.swd392.lmsbe.service.BorrowRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowRequestServiceImpl implements BorrowRequestService {

    private final BorrowRequestRepository borrowRequestRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public List<BorrowRequest> getAllRequests() {
        return borrowRequestRepository.findAll();
    }

    @Override
    public List<BorrowRequest> getPendingRequests() {
        return borrowRequestRepository.findByStatusOrderByRequestDateDesc(BorrowRequestStatus.PENDING.getValue());
    }

    @Override
    @Transactional
    public BorrowRequest sendRequest(CreateBorrowRequest request, String borrowerUsername) {
        User borrower = userRepository.findUserByUsername(borrowerUsername);
        if (borrower == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        if (book.getAvailableQuantity() <= 0) {
            throw new BadRequestException("Book is not available");
        }

        BorrowRequest borrowRequest = new BorrowRequest();
        borrowRequest.setBorrower(borrower);
        borrowRequest.setBook(book);
        borrowRequest.setNote(request.getNote());
        borrowRequest.setStatus(BorrowRequestStatus.PENDING.getValue());
        borrowRequest.setRequestDate(Instant.now());

        return borrowRequestRepository.save(borrowRequest);
    }

    @Override
    @Transactional
    public void approveRequest(Integer requestId, String staffUsername) {
        User staff = userRepository.findUserByUsername(staffUsername);
        if (staff == null) {
            throw new ResourceNotFoundException("Staff not found");
        }

        BorrowRequest borrowRequest = borrowRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow request not found"));

        if (!BorrowRequestStatus.PENDING.getValue().equals(borrowRequest.getStatus())) {
            throw new BadRequestException("Borrow request is not pending");
        }

        Book book = borrowRequest.getBook();

        if (!BookStatus.AVAILABLE.getValue().equals(book.getStatus()) || book.getAvailableQuantity() <= 0) {
            throw new BadRequestException("Book is not available for borrowing");
        }

        // Update BorrowRequest
        borrowRequest.setStatus(BorrowRequestStatus.APPROVED.getValue());
        borrowRequest.setStaff(staff);
        borrowRequest.setResponseDate(Instant.now());
        borrowRequestRepository.save(borrowRequest);

        // Create BorrowRecord
        BorrowRecord borrowRecord = new BorrowRecord();
        borrowRecord.setRequest(borrowRequest);
        borrowRecord.setBorrower(borrowRequest.getBorrower());
        borrowRecord.setBook(book);
        borrowRecord.setStaff(staff);
        
        Instant now = Instant.now();
        borrowRecord.setBorrowDate(now);
        // Assuming a loan period of 14 days if not configured elsewhere
        borrowRecord.setDueDate(now.plusSeconds(14L * 24 * 60 * 60)); 
        borrowRecord.setStatus(BorrowRecordStatus.BORROWING.getValue());
        
        borrowRecordRepository.save(borrowRecord);

        // Update Book
        book.setAvailableQuantity(book.getAvailableQuantity() - 1);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void rejectRequest(Integer requestId, String staffUsername) {
        User staff = userRepository.findUserByUsername(staffUsername);
        if (staff == null) {
            throw new ResourceNotFoundException("Staff not found");
        }

        BorrowRequest borrowRequest = borrowRequestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Borrow request not found"));

        if (!BorrowRequestStatus.PENDING.getValue().equals(borrowRequest.getStatus())) {
            throw new BadRequestException("Borrow request is not pending");
        }

        borrowRequest.setStatus(BorrowRequestStatus.REJECTED.getValue());
        borrowRequest.setStaff(staff);
        borrowRequest.setResponseDate(Instant.now());

        borrowRequestRepository.save(borrowRequest);
    }

    private Instant toInstant(LocalDate localDate) {
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
    }
}
