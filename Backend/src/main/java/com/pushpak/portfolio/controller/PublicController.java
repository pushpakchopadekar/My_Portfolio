package com.pushpak.portfolio.controller;

import com.pushpak.portfolio.model.*;
import com.pushpak.portfolio.repository.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Public, read-only endpoints consumed by the portfolio website (no login needed)
@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final ProfileRepository profileRepository;
    private final SkillRepository skillRepository;
    private final ExperienceRepository experienceRepository;
    private final ProjectRepository projectRepository;
    private final CertificationRepository certificationRepository;

    public PublicController(ProfileRepository profileRepository, SkillRepository skillRepository,
                             ExperienceRepository experienceRepository, ProjectRepository projectRepository,
                             CertificationRepository certificationRepository) {
        this.profileRepository = profileRepository;
        this.skillRepository = skillRepository;
        this.experienceRepository = experienceRepository;
        this.projectRepository = projectRepository;
        this.certificationRepository = certificationRepository;
    }

    @GetMapping("/profile")
    public Profile getProfile() {
        return profileRepository.findById(1L).orElse(new Profile());
    }

    @GetMapping("/skills")
    public List<Skill> getSkills() { return skillRepository.findAll(); }

    @GetMapping("/experience")
    public List<Experience> getExperience() { return experienceRepository.findAll(); }

    @GetMapping("/projects")
    public List<Project> getProjects() { return projectRepository.findAll(); }

    @GetMapping("/certifications")
    public List<Certification> getCertifications() { return certificationRepository.findAll(); }
}
