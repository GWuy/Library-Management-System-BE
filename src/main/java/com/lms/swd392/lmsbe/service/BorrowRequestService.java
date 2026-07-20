package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.entity.BorrowRequest;
import com.lms.swd392.lmsbe.model.request.ApproveBorrowRequest;
import com.lms.swd392.lmsbe.model.request.CreateBorrowRequest;

import java.util.List;

public interface BorrowRequestService {

    List<BorrowRequest> getAllRequests();
    
    List<BorrowRequest> getPendingRequests();

    BorrowRequest sendRequest(CreateBorrowRequest request, String borrowerUsername);

    void approveRequest(Integer requestId, String staffUsername);

    void rejectRequest(Integer requestId, String staffUsername);
}
