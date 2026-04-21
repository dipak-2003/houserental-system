package com.rental.houserental.controller;

import com.rental.houserental.config.JwtUtil;
import com.rental.houserental.dto.AuthResponse;
import com.rental.houserental.dto.LoginRequest;
import com.rental.houserental.dto.Notice;
import com.rental.houserental.dto.RegisterRequest;
import com.rental.houserental.entity.*;
import com.rental.houserental.enums.Role;
import com.rental.houserental.repository.AdminRepository;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.TenantRepository;
import com.rental.houserental.repository.TokenRepository;
import com.rental.houserental.service.NotificationProvider;
import com.rental.houserental.service.impl.EmailService;
import com.rental.houserental.service.impl.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationProvider notificationProvider;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private TenantRepository tenantRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.name}")
    private String adminName;

    @Value("${app.admin.password}")
    private String adminPassword;
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        // Check duplicate email
        if (tenantRepository.findByEmail(request.getEmail()).isPresent() ||
                adminRepository.findByEmail(request.getEmail()).isPresent() ||
                ownerRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }

        // ================= ADMIN AUTO CREATE =================
        if (adminRepository.findByEmail(adminEmail).isEmpty()) {
            Admin admin = new Admin();
            admin.setFullName(adminName);
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setRole(Role.ADMIN);
            adminRepository.save(admin);
        }
        int otp = (int)(Math.random() * 900000) + 100000;
        String token1 = String.valueOf(otp);
        Token token=new Token();
        token.setEmail(request.getEmail());
        token.setRole(request.getRole());
        token.setFullName(request.getFullName());
        token.setPassword(request.getPassword());
        token.setToken(token1);
        tokenRepository.save(token);
        System.out.println(token1);
        emailService.sendEmailVerifyToken(request.getEmail(),token1);
        return ResponseEntity.status(HttpStatus.CREATED).body(" Check your email and enter the token");
        }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        }
        catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid Email or Password");
        }

        Long id = null;
        String fullName = null;
        String role = null;
        String authEmail=null;
        String email = request.getEmail();

        // Check Admin
        Optional<Admin> adminOptional = adminRepository.findByEmail(email);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            id = admin.getId();
            fullName = admin.getFullName();
            role = admin.getRole().name();
            authEmail=admin.getEmail();
        }

        // Check Owner
        Optional<Owner> ownerOptional = ownerRepository.findByEmail(email);
        if (ownerOptional.isPresent()) {
            Owner owner = ownerOptional.get();
            id = owner.getId();
            fullName = owner.getFullName();
            role = owner.getRole().name();
            authEmail=owner.getEmail();
        }

        // Check Tenant
        Optional<Tenant> tenantOptional = tenantRepository.findByEmail(email);
        if (tenantOptional.isPresent()) {
            Tenant tenant = tenantOptional.get();
            id = tenant.getId();
            fullName = tenant.getFullName();
            role = tenant.getRole().name();
            authEmail=tenant.getEmail();
        }

        // Generate JWT with role
        String token = jwtUtil.generateToken(email, role);

        AuthResponse response = new AuthResponse(
                id,
                fullName,
                role,
                token,
                authEmail,
                "Login Successful"
        );

        return ResponseEntity.ok(response);
    }


    @PostMapping("/forget-password")
    public ResponseEntity<String> forgetPassword(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        Optional<Tenant> tenantOpt = tenantRepository.findByEmail(email);
        Optional<Owner> ownerOpt = ownerRepository.findByEmail(email);
        SecureRandom random = new SecureRandom();
        String token = String.format("%06d", random.nextInt(1000000));
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            owner.setResetToken(token);
            ownerRepository.save(owner);
            emailService.sendResetMail(owner.getFullName(), owner.getEmail(), token);
            return ResponseEntity.ok("Reset link sent to owner");
        }

        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setResetToken(token);
            adminRepository.save(admin);
            emailService.sendResetMail(admin.getFullName(), admin.getEmail(), token);
            return ResponseEntity.ok("Reset link sent to admin");
        }

        if (tenantOpt.isPresent()) {
            Tenant tenant = tenantOpt.get();
            tenant.setResetToken(token);
            tenantRepository.save(tenant);
            emailService.sendResetMail(tenant.getFullName(), tenant.getEmail(), token);
            return ResponseEntity.ok("Reset link sent to tenant");
        }
        return ResponseEntity.badRequest().body("Email not found");
    }

    @PutMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {

        String token = request.get("token");
        String password = request.get("password");

        Admin admin = adminRepository.findByResetToken(token);
        Owner owner = ownerRepository.findByResetToken(token);
        Tenant tenant = tenantRepository.findByResetToken(token);

        String encodePass = passwordEncoder.encode(password);

        if (admin != null) {
            admin.setPassword(encodePass);
            admin.setResetToken(null); // clear token
            return new ResponseEntity<>(adminRepository.save(admin), HttpStatus.OK);
        }

        if (owner != null) {
            owner.setPassword(encodePass);
            owner.setResetToken(null);
            return new ResponseEntity<>(ownerRepository.save(owner), HttpStatus.OK);
        }

        if (tenant != null) {
            tenant.setPassword(encodePass);
            tenant.setResetToken(null);
            return new ResponseEntity<>(tenantRepository.save(tenant), HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
    }


    @PutMapping("/verify-email/{token}")
    public ResponseEntity<?> verifyEmailToken(@PathVariable String token){

      Token token1=tokenRepository.findByToken(token).get();
      if(token==null){
          return new ResponseEntity<>("Invalid email token",HttpStatus.BAD_REQUEST);
      }

      String password=passwordEncoder.encode(token1.getPassword());
      if(token1.getRole()==Role.OWNER){
          Owner owner=new Owner();
          owner.setEmail(token1.getEmail());
          owner.setPassword(password);
          owner.setRole(Role.OWNER);
          owner.setFullName(token1.getFullName());
          System.out.println(owner);
          ownerRepository.save(owner);
          return ResponseEntity.ok("Owner register successfully!");
      }


      Tenant tenant=new Tenant();
      tenant.setEmail(token1.getEmail());
      tenant.setFullName(token1.getFullName());
      tenant.setRole(Role.TENANT);
      tenant.setPassword(password);
      tenantRepository.save(tenant);
      List<Admin> admins = adminRepository.findAll();
      Notice notice=notificationProvider.adminNewCustomer();
      for (Admin admin:admins) {
          Notification notification = new Notification();
          notification.setMessage(notice.getMessage());
          notification.setTitle(notice.getTitle());
          notification.setRole(Role.ADMIN);
          notification.setUserId(admin.getId());
          notificationService.create(notification);
      }
      return new ResponseEntity<>("Tenant register successfully!",HttpStatus.OK);

    }


}