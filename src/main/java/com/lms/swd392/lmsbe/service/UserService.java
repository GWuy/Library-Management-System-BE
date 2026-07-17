package com.lms.swd392.lmsbe.service;

import com.lms.swd392.lmsbe.entity.User;
import com.lms.swd392.lmsbe.model.request.RegisterRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User register(RegisterRequest registerRequest, MultipartFile avatarFile);
    User findByUsername(String username);
    User findById(Integer id);
}
