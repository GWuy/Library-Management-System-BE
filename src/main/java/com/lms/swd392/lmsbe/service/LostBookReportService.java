package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.entity.LostBookReport;
import com.lms.swd392.lmsbe.model.request.ReportLostBookRequest;

import java.util.List;

public interface LostBookReportService {
    void reportLostBook(ReportLostBookRequest request, String username);
    List<LostBookReport> getAllReports();
    void updateReportStatus(Integer reportId, String status, String staffUsername);
}
