package com.lms.swd392.lmsbe.repository;

import com.lms.swd392.lmsbe.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {

    List<BorrowRecord> findByStatusOrderByBorrowDateDesc(String status);
}
