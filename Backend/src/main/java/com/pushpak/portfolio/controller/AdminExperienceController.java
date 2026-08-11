package com.pushpak.portfolio.controller;

import com.pushpak.portfolio.model.Experience;
import com.pushpak.portfolio.repository.ExperienceRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/experience")
public class AdminExperienceController {

    private final ExperienceRepository repo;
    public AdminExperienceController(ExperienceRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Experience> getAll() { return repo.findAll(); }

    @PostMapping
    public Experience create(@RequestBody Experience e) { return repo.save(e); }

    @PutMapping("/{id}")
    public Experience update(@PathVariable Long id, @RequestBody Experience e) {
        e.setId(id);
        return repo.save(e);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
