package com.reverso.service;

import com.reverso.service.impl.FileStorageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FileStorageServiceImplTest {

    private FileStorageServiceImpl fileStorageService;

    @Mock
    private MultipartFile multipartFile;

    @TempDir
    Path tempDir; 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        fileStorageService = new FileStorageServiceImpl() {
            @Override
            public String store(MultipartFile file, String folder) {
                try {
                    Field uploadRootField = FileStorageServiceImpl.class.getDeclaredField("uploadRoot");
                    uploadRootField.setAccessible(true);
                    Path backup = (Path) uploadRootField.get(this); 
                    uploadRootField.set(this, tempDir);
                    String result = super.store(file, folder);
                    uploadRootField.set(this, backup);
                    return result;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    @Test
    void store_shouldReturnPublicUrl_whenFileIsValid() throws IOException {
        when(multipartFile.isEmpty()).thenReturn(false);
        when(multipartFile.getOriginalFilename()).thenReturn("test.txt");

        doAnswer(invocation -> {
            Path target = invocation.getArgument(0, File.class).toPath();
            Files.writeString(target, "contenido de prueba");
            return null;
        }).when(multipartFile).transferTo(any(File.class));

        String folder = "subfolder";
        String url = fileStorageService.store(multipartFile, folder);

        assertThat(url).startsWith("/uploads/subfolder/");
        Path filePath = tempDir.resolve("subfolder").resolve(url.substring(url.lastIndexOf('/') + 1));
        assertThat(Files.exists(filePath)).isTrue();
        assertThat(Files.readString(filePath)).isEqualTo("contenido de prueba");
    }

    @Test
    void store_shouldReturnNull_whenFileIsNull() {
        String url = fileStorageService.store(null, "folder");
        assertThat(url).isNull();
    }

    @Test
    void store_shouldReturnNull_whenFileIsEmpty() {
        when(multipartFile.isEmpty()).thenReturn(true);
        String url = fileStorageService.store(multipartFile, "folder");
        assertThat(url).isNull();
    }


    @Test
    void delete_shouldDoNothingIfFileDoesNotExist() {
        String fileUrl = "/uploads/nonexistent.txt";
        fileStorageService.delete(fileUrl); 
    }

    @Test
    void delete_shouldDoNothingIfFileUrlIsNullOrBlank() {
        fileStorageService.delete(null);
        fileStorageService.delete("");
        fileStorageService.delete("   ");
    }
}