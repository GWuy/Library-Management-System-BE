package com.lms.swd392.lmsbe.repository;
  
import com.lms.swd392.lmsbe.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {

    List<BorrowRecord> findByStatusOrderByBorrowDateDesc(String status);

    List<BorrowRecord> findByStatusIn(List<String> statuses);

    Optional<BorrowRecord> findByBorrower_IdAndBook_IdAndStatus(Integer borrowerId, Integer bookId, String status);

    Optional<BorrowRecord> findByIdAndBorrower_Id(Integer borrowId, Integer borrowerId);
}
