package com.lms.swd392.lmsbe.constant;

import lombok.Getter;

@Getter
public enum BorrowRecordStatus {

    BORROWING("BORROWING"),
    RETURNED("RETURNED"),
    OVERDUE("OVERDUE"),
    LOST("LOST");

    private final String value;

    BorrowRecordStatus(String value) {
        this.value = value;
    }
}
