package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.entity.BorrowRequest;
import com.lms.swd392.lmsbe.model.request.ApproveBorrowRequest;
import com.lms.swd392.lmsbe.model.request.CreateBorrowRequest;

import java.util.List;

public interface BorrowRequestService {

    List<BorrowRequest> getAllRequests();

    BorrowRequest sendRequest(CreateBorrowRequest request, String borrowerUsername);

    BorrowRequest approveRequest(Integer id, ApproveBorrowRequest request, String staffUsername);

    BorrowRequest rejectRequest(Integer id, String staffUsername);
}
