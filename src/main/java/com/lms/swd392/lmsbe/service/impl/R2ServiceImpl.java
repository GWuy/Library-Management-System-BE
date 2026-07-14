package com.lms.swd392.lmsbe.service.impl;

import com.lms.swd392.lmsbe.exception.BadRequestException;
import com.lms.swd392.lmsbe.exception.ConflictException;
import com.lms.swd392.lmsbe.service.R2Service;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class R2ServiceImpl implements R2Service {

    final S3Client s3Client;

    @Value("${cloudflare.r2.bucket-name}")
    private String bucketName;

    @Value("${cloudflare.r2.public-base-url}")
    private String publicBaseUrl;

    @Override
    public String uploadFile(
            MultipartFile file,
            String folderName
    ) {

        // 2. Validate file
        if (file == null || file.isEmpty()) {
            throw new ConflictException("Image mustn't be empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.matches("image/(png|jpeg|webp)")) {
            throw new ConflictException(
                    "File must be image type"
            );
        }

        // 4. Build object key
        String fileKey = folderName + "/"
                + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            // 5. Upload to R2
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(fileKey)
                            .contentType(contentType)
                            .acl(ObjectCannedACL.PUBLIC_READ)
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );

            return publicBaseUrl + "/" + fileKey;

        } catch (IOException e) {
            throw new BadRequestException(
                    "Failed to upload file"
            );
        }
    }
}
