package com.lms.swd392.lmsbe.model.request;

import com.lms.swd392.lmsbe.validation.UserValidation;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email is invalid")
    @Pattern(regexp = UserValidation.EMAIL_REGEX, message = "Email is invalid")
    String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = UserValidation.VIET_NAM_PHONE_REGEX, message = "Phone number is invalid")
    String phone;

    @NotBlank(message = "Username is required")
    String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 32, message = "Password must be between 8 and 32 characters")
    @Pattern(regexp = UserValidation.PASSWORD_REGEX, message = "Password must contain at least one uppercase letter, one lowercase letter, one number and one special character")
    String password;
}
