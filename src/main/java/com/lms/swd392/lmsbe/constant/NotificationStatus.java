package com.lms.swd392.lmsbe.constant;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    PENDING("PENDING"),
    SENT("SENT"),
    FAILED("FAILED");

    private final String value;

    NotificationStatus(String value) {
        this.value = value;
    }
}
