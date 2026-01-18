package com.reverso.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.reverso.service.interfaces.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Profile("prod")
public class CloudinaryStorageServiceImpl implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryStorageServiceImpl.class);

    private final Cloudinary cloudinary;

    public CloudinaryStorageServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            log.warn("Cloudinary store() called with null or empty file. folder={}", folder);
            return null;
        }

        try {
            String safeFolder = (folder == null || folder.isEmpty()) ? "reverso_social" : folder;

            // Upload parameters
            // We let Cloudinary generate a random ID to avoid collisions, but put it in a
            // folder.
            @SuppressWarnings("rawtypes")
            Map params = ObjectUtils.asMap(
                    "folder", safeFolder,
                    "resource_type", "auto");

            log.info("Uploading file to Cloudinary. Folder: {}", safeFolder);

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), params);

            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("Upload successful. URL: {}", secureUrl);

            return secureUrl;

        } catch (IOException e) {
            log.error("Cloudinary upload failed", e);
            throw new RuntimeException("Cloudinary upload failed", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank())
            return;

        try {
            // Extract public_id from URL

            String publicId = extractPublicIdFromUrl(fileUrl);
            if (publicId == null) {
                log.warn("Could not extract publicId from URL: {}", fileUrl);
                return;
            }

            log.info("Deleting file from Cloudinary. Public ID: {}", publicId);
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());

        } catch (Exception e) {
            log.error("Cloudinary delete failed: {}", fileUrl, e);
            throw new RuntimeException("Cloudinary delete failed", e);
        }
    }

    private String extractPublicIdFromUrl(String url) {
        try {
            // Typical structure: .../upload/v<version>/<folder>/<id>.<extension>

            int uploadIndex = url.indexOf("/upload/");
            if (uploadIndex == -1)
                return null;

            String afterUpload = url.substring(uploadIndex + 8);

            if (afterUpload.startsWith("v") && afterUpload.indexOf("/") > 0) {
                int slashIndex = afterUpload.indexOf("/");
                afterUpload = afterUpload.substring(slashIndex + 1);
            }

            int dotIndex = afterUpload.lastIndexOf(".");
            if (dotIndex != -1) {
                afterUpload = afterUpload.substring(0, dotIndex);
            }

            return afterUpload;
        } catch (Exception e) {
            return null;
        }
    }
}
