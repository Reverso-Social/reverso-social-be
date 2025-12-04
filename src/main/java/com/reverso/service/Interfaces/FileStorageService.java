package com.reverso.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveBlogImage(MultipartFile file);
}
