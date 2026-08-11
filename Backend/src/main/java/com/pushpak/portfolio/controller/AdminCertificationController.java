package com.pushpak.portfolio.controller;

import com.pushpak.portfolio.model.Certification;
import com.pushpak.portfolio.repository.CertificationRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/certifications")
public class AdminCertificationController {

    private final CertificationRepository repo;
    public AdminCertificationController(CertificationRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Certification> getAll() { return repo.findAll(); }

    @PostMapping
    public Certification create(@RequestBody Certification c) { return repo.save(c); }

    @PutMapping("/{id}")
    public Certification update(@PathVariable Long id, @RequestBody Certification c) {
        c.setId(id);
        return repo.save(c);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
