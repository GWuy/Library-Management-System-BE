package com.lms.swd392.lmsbe.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    Integer id;
    String fullName;
    String email;
    String phone;
    String username;
    String role;
    String status;
    String avatarUrl;
}
