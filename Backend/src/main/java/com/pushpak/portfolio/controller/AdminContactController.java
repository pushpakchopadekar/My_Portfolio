package com.pushpak.portfolio.controller;

import com.pushpak.portfolio.model.ContactMessage;
import com.pushpak.portfolio.repository.ContactMessageRepository;
import com.pushpak.portfolio.service.MailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AdminContactController {

    private final ContactMessageRepository repo;
    private final MailService mailService;

    public AdminContactController(ContactMessageRepository repo, MailService mailService) {
        this.repo = repo;
        this.mailService = mailService;
    }

    // Public: the contact form on the portfolio submits here.
    // Saves the message AND emails a notification to the site owner.
    @PostMapping("/api/public/contact")
    public ContactMessage submit(@RequestBody ContactMessage msg) {
        msg.setId(null);
        ContactMessage saved = repo.save(msg);
        mailService.sendContactNotification(msg.getName(), msg.getEmail(), msg.getMessage());
        return saved;
    }

    // Admin: view all messages received via the contact form
    @GetMapping("/api/admin/messages")
    public List<ContactMessage> getAll() { return repo.findAll(); }

    @DeleteMapping("/api/admin/messages/{id}")
    public void delete(@PathVariable Long id) { repo.deleteById(id); }
}
