package com.rental.houserental.config;

import com.rental.houserental.entity.Admin;
import com.rental.houserental.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class AdminSeeder {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void createAdminIfNotExists() {

        if (adminRepository.findByEmail(adminEmail).isPresent()) {
            return; // Admin already exists
        }

        Admin admin = new Admin();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole("ROLE_ADMIN");

        adminRepository.save(admin);

        System.out.println("Default Admin Created: " + adminEmail);
    }
}
