package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.model.request.ReportLostBookRequest;

public interface LostBookReportService {
    void reportLostBook(ReportLostBookRequest request, String username);
}
