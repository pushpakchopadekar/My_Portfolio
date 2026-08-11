package com.pushpak.portfolio.controller;

import com.pushpak.portfolio.model.Project;
import com.pushpak.portfolio.repository.ProjectRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/projects")
public class AdminProjectController {

    private final ProjectRepository repo;
    public AdminProjectController(ProjectRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Project> getAll() { return repo.findAll(); }

    @PostMapping
    public Project create(@RequestBody Project p) { return repo.save(p); }

    @PutMapping("/{id}")
    public Project update(@PathVariable Long id, @RequestBody Project p) {
        p.setId(id);
        return repo.save(p);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
