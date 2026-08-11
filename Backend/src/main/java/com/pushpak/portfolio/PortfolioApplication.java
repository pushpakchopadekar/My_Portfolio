package com.pushpak.portfolio;

import com.pushpak.portfolio.model.Admin;
import com.pushpak.portfolio.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class PortfolioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Creates the default admin user on first run if none exists
    @Bean
    public CommandLineRunner initAdmin(AdminRepository adminRepository,
                                        PasswordEncoder encoder,
                                        @Value("${admin.default.username}") String defaultUsername,
                                        @Value("${admin.default.password}") String defaultPassword) {
        return args -> {
            if (adminRepository.count() == 0) {
                Admin admin = new Admin();
                admin.setUsername(defaultUsername);
                admin.setPassword(encoder.encode(defaultPassword));
                adminRepository.save(admin);
                System.out.println("Default admin created -> username: " + defaultUsername);
            }
        };
    }
}
