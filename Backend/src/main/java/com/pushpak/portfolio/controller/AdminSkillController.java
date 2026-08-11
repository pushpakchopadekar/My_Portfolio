package com.pushpak.portfolio.controller;

import com.pushpak.portfolio.model.Skill;
import com.pushpak.portfolio.repository.SkillRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/skills")
public class AdminSkillController {

    private final SkillRepository repo;
    public AdminSkillController(SkillRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Skill> getAll() { return repo.findAll(); }

    @PostMapping
    public Skill create(@RequestBody Skill skill) { return repo.save(skill); }

    @PutMapping("/{id}")
    public Skill update(@PathVariable Long id, @RequestBody Skill skill) {
        skill.setId(id);
        return repo.save(skill);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
