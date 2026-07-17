package com.lms.swd392.lmsbe.constant;

import lombok.Getter;

@Getter
public enum BorrowRequestStatus {

    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED");

    private final String value;

    BorrowRequestStatus(String value) {
        this.value = value;
    }
}
