package com.pushpak.portfolio.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

// Public endpoint serves the current profile photo; admin can replace it.
@RestController
public class PhotoController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String PHOTO_FILENAME = "photo.jpg";

    @GetMapping("/api/public/photo")
    public ResponseEntity<Resource> getPhoto() throws IOException {
        Path path = Paths.get(uploadDir, PHOTO_FILENAME);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .header("Cache-Control", "no-cache")
                .body(resource);
    }

    @PostMapping("/api/admin/photo")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        Path path = Paths.get(uploadDir, PHOTO_FILENAME);
        Files.write(path, file.getBytes());
        return ResponseEntity.ok("Photo uploaded successfully");
    }
}
