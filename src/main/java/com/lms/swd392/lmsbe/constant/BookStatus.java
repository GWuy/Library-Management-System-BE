package com.lms.swd392.lmsbe.constant;

import lombok.Getter;

@Getter
public enum BookStatus {

    AVAILABLE("AVAILABLE"),
    BORROWED("BORROWED"),
    UNAVAILABLE("UNAVAILABLE"),
    DAMAGE("DAMAGE"),
    REMOVE("REMOVE");

    private final String value;

    BookStatus(String value) {
        this.value = value;
    }
}
