package com.pushpak.portfolio.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

// Public download endpoint + admin upload endpoint for the resume PDF
@RestController
public class ResumeController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private static final String RESUME_FILENAME = "resume.pdf";

    // Anyone can download the current resume — used by the "Download Resume" button
    @GetMapping("/api/public/resume")
    public ResponseEntity<Resource> downloadResume() throws IOException {
        Path path = Paths.get(uploadDir, RESUME_FILENAME);
        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }
        Resource resource = new FileSystemResource(path);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Pushpak-Chopadekar-Resume.pdf\"")
                .body(resource);
    }

    // Admin (JWT protected via SecurityConfig: /api/admin/**) uploads/replaces the resume
    @PostMapping("/api/admin/resume")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();
        Path path = Paths.get(uploadDir, RESUME_FILENAME);
        Files.write(path, file.getBytes());
        return ResponseEntity.ok("Resume uploaded successfully");
    }
}
