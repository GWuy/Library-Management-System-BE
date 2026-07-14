package com.lms.swd392.lmsbe.validation;

import com.lms.swd392.lmsbe.exception.BadRequestException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileValidator {

    private static final List<String> ALLOWED_IMAGE_TYPES = List.of("image/jpeg", "image/jpg", "image/png", "image/webp");
    private static final long MIN_SIZE = 1024; // 1KB
    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    public void validateAvatar(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("Avatar is required");
        }

        if (file.getSize() < MIN_SIZE || file.getSize() > MAX_SIZE) {
            throw new BadRequestException("Avatar size must be between 1 KB and 5 MB");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new BadRequestException("Only JPG, JPEG, PNG and WEBP images are allowed");
        }
    }
}
