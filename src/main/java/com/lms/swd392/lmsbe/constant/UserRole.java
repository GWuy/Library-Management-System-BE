package com.lms.swd392.lmsbe.constant;

import lombok.Getter;

@Getter
public enum UserRole {
    BORROWER("BORROWER"),
    STAFF("STAFF");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }
}
