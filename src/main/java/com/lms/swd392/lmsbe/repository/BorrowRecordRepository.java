package com.lms.swd392.lmsbe.repository;
  
import com.lms.swd392.lmsbe.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {

    List<BorrowRecord> findByStatusOrderByBorrowDateDesc(String status);

    Optional<BorrowRecord> findByBorrower_IdAndBook_IdAndStatus(Integer borrowerId, Integer bookId, String status);
}
