package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.model.request.LoginRequest;
import com.lms.swd392.lmsbe.model.request.RefreshTokenRequest;
import com.lms.swd392.lmsbe.model.response.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);

    LoginResponse refreshToken(RefreshTokenRequest request);

    void logout(String username);
}
