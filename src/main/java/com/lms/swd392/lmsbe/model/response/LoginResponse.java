package com.lms.swd392.lmsbe.model.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String accessToken;
    String refreshToken;

    @Builder.Default
    String tokenType = "Bearer";

    long expiresIn;
    UserResponse user;
}
