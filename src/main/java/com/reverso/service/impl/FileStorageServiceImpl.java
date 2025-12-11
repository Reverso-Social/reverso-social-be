package com.reverso.service.impl;

import com.reverso.service.interfaces.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final String uploadRoot = "uploads"; // carpeta raíz en proyecto

    @Override
    public String store(MultipartFile file, String folder) {
        try {
            Path uploadPath = Paths.get(uploadRoot, folder);
            Files.createDirectories(uploadPath);

            // Siempre nombre único (timestamp + nombre original)
            String originalName = file.getOriginalFilename();
            String filename = System.currentTimeMillis() + "-" + originalName;

            Path filePath = uploadPath.resolve(filename);

            // Guardar archivo en disco
            file.transferTo(filePath.toFile());

            // URL accesible por el front
            return "/uploads/" + folder + "/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Error al guardar archivo", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        try {
            // /uploads/blog/123.webp → uploads/blog/123.webp
            String relative = fileUrl.replace("/uploads", "");
            Path filePath = Paths.get("uploads" + relative);

            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            throw new RuntimeException("Error al borrar archivo", e);
        }
    }
}
