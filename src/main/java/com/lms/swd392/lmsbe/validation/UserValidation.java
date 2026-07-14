package com.lms.swd392.lmsbe.validation;

public final class UserValidation {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    public static final String VIET_NAM_PHONE_REGEX = "^(\\+84|0)(3|5|7|8|9)[0-9]{8}$";
    public static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&^#()_+\\-=\\[\\]{};':\"\\\\|,.<>/?])[A-Za-z\\d@$!%*?&^#()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]{8,32}$";
}
