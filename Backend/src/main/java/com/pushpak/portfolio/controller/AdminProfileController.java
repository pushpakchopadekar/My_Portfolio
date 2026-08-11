package com.pushpak.portfolio.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pushpak.portfolio.model.Profile;
import com.pushpak.portfolio.repository.ProfileRepository;

@RestController
@RequestMapping("/api/admin/profile")
public class AdminProfileController {

    private final ProfileRepository profileRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public AdminProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping
    public Profile get() {
        return profileRepository.findById(1L).orElse(new Profile());
    }

    @PutMapping
    public Profile update(@RequestBody Profile profile) {
        profile.setId(1L);
        return profileRepository.save(profile);
    }

    // Upload/replace the profile photo shown in the hero section
    @PostMapping("/photo")
    public ResponseEntity<Profile> uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists())
            dir.mkdirs();

        String ext = ".jpg";
        String original = file.getOriginalFilename();
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = "profile-photo" + ext;
        Path path = Paths.get(uploadDir, filename);
        Files.write(path, file.getBytes());

        Profile profile = profileRepository.findById(1L).orElse(new Profile());
        profile.setId(1L);
        profile.setPhotoUrl("/uploads/" + filename);
        Profile saved = profileRepository.save(profile);
        return ResponseEntity.ok(saved);
    }
}
