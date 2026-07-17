package com.lms.swd392.lmsbe.mapper;

import com.lms.swd392.lmsbe.entity.BorrowRequest;
import com.lms.swd392.lmsbe.model.response.BorrowRequestResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BorrowRequestMapper {

    @Mapping(source = "book.title", target = "bookTitle")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "borrower.fullName", target = "borrowerName")
    @Mapping(source = "borrower.id", target = "borrowerId")
    @Mapping(source = "staff.fullName", target = "staffName")
    @Mapping(source = "staff.id", target = "staffId")
    BorrowRequestResponse toBorrowRequestResponse(BorrowRequest borrowRequest);
}
