package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.constant.BorrowRecordStatus;
import com.lms.swd392.lmsbe.constant.LostReportStatus;
import com.lms.swd392.lmsbe.entity.BorrowRecord;
import com.lms.swd392.lmsbe.entity.LostBookReport;
import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.exception.BadRequestException;
import com.lms.swd392.lmsbe.exception.ResourceNotFoundException;
import com.lms.swd392.lmsbe.model.request.ReportLostBookRequest;
import com.lms.swd392.lmsbe.repository.BorrowRecordRepository;
import com.lms.swd392.lmsbe.repository.LostBookReportRepository;
import com.lms.swd392.lmsbe.repository.UserRepository;
import com.lms.swd392.lmsbe.service.LostBookReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class LostBookReportServiceImpl implements LostBookReportService {

    private final LostBookReportRepository lostBookReportRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void reportLostBook(ReportLostBookRequest request, String username) {
        User borrower = userRepository.findUserByUsername(username);
        if (borrower == null) {
            throw new ResourceNotFoundException("User not found");
        }

        BorrowRecord borrowRecord = borrowRecordRepository.findByIdAndBorrower_Id(request.getBorrowId(), borrower.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Borrow record not found"));

        if (!BorrowRecordStatus.BORROWING.getValue().equals(borrowRecord.getStatus())) {
            throw new BadRequestException("Cannot report lost book.");
        }

        // Create LostBookReport
        LostBookReport lostBookReport = new LostBookReport();
        lostBookReport.setBorrow(borrowRecord);
        lostBookReport.setBorrower(borrower);
        lostBookReport.setBook(borrowRecord.getBook());
        lostBookReport.setDescription(request.getDescription());
        lostBookReport.setReportDate(Instant.now());
        lostBookReport.setStatus(LostReportStatus.PENDING.getValue());

        lostBookReportRepository.save(lostBookReport);

        // Update BorrowRecord
        borrowRecord.setStatus(BorrowRecordStatus.LOST.getValue());
        borrowRecordRepository.save(borrowRecord);
    }
}
