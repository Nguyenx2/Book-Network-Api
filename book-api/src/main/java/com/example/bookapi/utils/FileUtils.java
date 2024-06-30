package com.example.bookapi.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

public class FileUtils {
    private static final String UPLOAD_DIR = "uploads";
    private static final String NOT_FOUND_IMAGE = "not-found.png";

    public static String storeFile(MultipartFile file, Long id) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String uniqueFileName = UUID.randomUUID() + "_" + fileName;
        Path uploadDir = Path.of(UPLOAD_DIR, id.toString());
        try {
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path destination = Path.of(uploadDir.toString(), uniqueFileName);

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not create directory " + uploadDir + ". Please try again!", e);
        }
    }

    public static Resource getFile(String path) {
        try {
            Path filePath = Path.of(path);
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                Path notFoundPath = Path.of(UPLOAD_DIR, NOT_FOUND_IMAGE);
                return new UrlResource(notFoundPath.toUri());
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not read file " + path + ". Please try again!", e);
        }
    }

    public static void deleteFile(String path) {
        try {
            Path filePath = Path.of(path);
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            throw new RuntimeException("Could not delete file " + path + ". Please try again!", e);
        }
    }
}
