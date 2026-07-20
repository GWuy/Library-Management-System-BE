package com.lms.swd392.lmsbe.repository;

import com.lms.swd392.lmsbe.entity.LostBookReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LostBookReportRepository extends JpaRepository<LostBookReport, Integer> {
}
