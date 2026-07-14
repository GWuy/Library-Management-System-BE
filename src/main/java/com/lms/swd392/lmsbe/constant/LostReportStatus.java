package com.lms.swd392.lmsbe.constant;

import lombok.Getter;

@Getter
public enum LostReportStatus {

    PENDING("PENDING"),
    PROCESSING("PROCESSING"),
    RESOLVED("RESOLVED");

    private final String value;

    LostReportStatus(String value) {
        this.value = value;
    }
}
