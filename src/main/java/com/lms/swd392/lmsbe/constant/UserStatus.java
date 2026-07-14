package com.lms.swd392.lmsbe.constant;

import lombok.Getter;

@Getter
public enum UserStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }
}
