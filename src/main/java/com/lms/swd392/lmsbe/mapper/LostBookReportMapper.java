package com.lms.swd392.lmsbe.mapper;

import com.lms.swd392.lmsbe.entity.LostBookReport;
import com.lms.swd392.lmsbe.model.response.LostBookReportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LostBookReportMapper {

    @Mapping(source = "borrow.id", target = "borrowId")
    @Mapping(source = "borrow.book.title", target = "bookTitle")
    @Mapping(source = "borrower.fullName", target = "borrowerName")
    LostBookReportResponse toResponse(LostBookReport lostBookReport);
}
