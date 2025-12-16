package com.reverso.service.impl;

import com.reverso.service.interfaces.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final Logger log = LoggerFactory.getLogger(FileStorageServiceImpl.class);

    private final Path uploadRoot = Paths.get("uploads").toAbsolutePath().normalize();

    @Override
    public String store(MultipartFile file, String folder) {
        if (file == null || file.isEmpty()) {
            log.warn("store() llamado con file null o vacío. folder={}", folder);
            return null;
        }

        try {
            String safeFolder = (folder == null) ? "" : folder.replaceFirst("^[\\\\/]+", "");
            Path uploadPath = uploadRoot.resolve(safeFolder).normalize();
            log.info("Guardando archivo en carpeta: {}", uploadPath);

            Files.createDirectories(uploadPath);

  
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.isBlank()) {
                originalName = "file";
            }

            String filename = System.currentTimeMillis() + "-" + originalName;
            Path filePath = uploadPath.resolve(filename);

            log.info("Ruta final del archivo: {}", filePath);


            file.transferTo(filePath.toFile());

            String publicUrl = "/uploads/" + (safeFolder.isEmpty() ? "" : safeFolder + "/") + filename;
            log.info("Archivo guardado correctamente. URL pública: {}", publicUrl);

            return publicUrl;

        } catch (IOException e) {
            log.error("Error al guardar archivo en disco. folder={}, originalName={}",
                    folder, file.getOriginalFilename(), e);
            throw new RuntimeException("Error al guardar archivo", e);
        }
    }

    @Override
    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) return;

        try {
            String relative = fileUrl.replace("/uploads", "");
            Path filePath = uploadRoot.resolve(relative.replaceFirst("^[\\\\/]+", "")).normalize();

            log.info("Intentando borrar archivo: {}", filePath);
            Files.deleteIfExists(filePath);

        } catch (IOException e) {
            log.error("Error al borrar archivo: {}", fileUrl, e);
            throw new RuntimeException("Error al borrar archivo", e);
        }
    }
}
