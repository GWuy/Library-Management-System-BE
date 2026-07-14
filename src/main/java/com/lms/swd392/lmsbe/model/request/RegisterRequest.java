package com.lms.swd392.lmsbe.model.request;

import com.lms.swd392.lmsbe.validation.UserValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterRequest {
    @NotNull
    String fullName;
    @Email(message = "Invalid email")
    @NotNull
    String email;
    @NotNull
    @Pattern(
            regexp = UserValidation.VIET_NAM_PHONE_REGEX,
            message = "Invalid phone number"
    )
    String phone;
    @NotNull
    String username;
    @NotNull
    String password;
}
