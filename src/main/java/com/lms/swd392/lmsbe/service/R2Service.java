package com.lms.swd392.lmsbe.service;

import org.springframework.web.multipart.MultipartFile;

public interface R2Service {

    String uploadFile(MultipartFile file, String folderName);
}
