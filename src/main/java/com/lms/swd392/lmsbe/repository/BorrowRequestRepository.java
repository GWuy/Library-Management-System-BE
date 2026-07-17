package com.lms.swd392.lmsbe.repository;

import com.lms.swd392.lmsbe.entity.BorrowRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BorrowRequestRepository extends JpaRepository<BorrowRequest, Integer> {

    List<BorrowRequest> findByStatusOrderByRequestDateDesc(String status);
}
