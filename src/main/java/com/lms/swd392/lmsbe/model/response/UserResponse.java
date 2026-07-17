package com.lms.swd392.lmsbe.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    Integer id;
    String fullName;
    String email;
    String phone;
    String username;
    String role;
    String status;
    String avatarUrl;
}
