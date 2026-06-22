package com.rental.houserental;

import com.rental.houserental.entity.Admin;
import com.rental.houserental.enums.Role;
import com.rental.houserental.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class HouseRentalBackendApplication implements CommandLineRunner {

	@Value("${app.admin.email}")
	private String adminEmail;

	@Value("${app.admin.name}")
	private String adminName;

	@Value("${app.admin.password}")
	private String adminPassword;

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HouseRentalBackendApplication.class, args);
	}

	@Override
public void run(String... args) {
		if (adminRepository.findByEmail(adminEmail).isEmpty()) {
			Admin admin = new Admin();
			admin.setFullName(adminName);
			admin.setEmail(adminEmail);
			admin.setPassword(passwordEncoder.encode(adminPassword));
			admin.setRole(Role.ADMIN);

			adminRepository.save(admin);

			System.out.println("Admin created successfully.");
		}
}
}