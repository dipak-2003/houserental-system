package com.rental.houserental.service;

import com.rental.houserental.config.JwtUtil;
import com.rental.houserental.dto.LoggedUser;
import com.rental.houserental.entity.Admin;
import com.rental.houserental.entity.Owner;
import com.rental.houserental.entity.Tenant;
import com.rental.houserental.repository.AdminRepository;
import com.rental.houserental.repository.OwnerRepository;
import com.rental.houserental.repository.TenantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetails implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private TenantRepository tenantRepository;

    // Load by email
    public UserDetails loadUserByEmail(String email) {
        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) return createUserDetails(adminOpt.get().getEmail(), adminOpt.get().getPassword(), adminOpt.get().getRole().name());

        Optional<Owner> ownerOpt = ownerRepository.findByEmail(email);
        if (ownerOpt.isPresent()) return createUserDetails(ownerOpt.get().getEmail(), ownerOpt.get().getPassword(), ownerOpt.get().getRole().name());

        Optional<Tenant> tenantOpt = tenantRepository.findByEmail(email);
        if (tenantOpt.isPresent()) return createUserDetails(tenantOpt.get().getEmail(), tenantOpt.get().getPassword(), tenantOpt.get().getRole().name());

        throw new UsernameNotFoundException("User not found: " + email);
    }

    private UserDetails createUserDetails(String email, String password, String role) {
        return User.builder()
                .username(email)
                .password(password)
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + role)))
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByEmail(username);
    }

    public LoggedUser loadUserByToken(String token) throws Exception {
        if (token == null || !token.startsWith("Bearer ")) {
            return null;
        }

        String jwt = token.substring(7).trim();

        // Validate token
        if (!jwtUtil.validateToken(jwt)) {
            throw new Exception("Token is invalid or expired");
        }

        String email = jwtUtil.extractEmail(jwt);

        Optional<Admin> adminOpt = adminRepository.findByEmail(email);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            LoggedUser user = new LoggedUser();
            user.setId(admin.getId());
            user.setFullName(admin.getFullName());
            user.setRole(admin.getRole());
            return user;
        }

        Optional<Owner> ownerOpt = ownerRepository.findByEmail(email);
        if (ownerOpt.isPresent()) {
            Owner owner = ownerOpt.get();
            LoggedUser user = new LoggedUser();
            user.setId(owner.getId());
            user.setFullName(owner.getFullName());
            user.setRole(owner.getRole());
            return user;
        }

        Optional<Tenant> tenantOpt = tenantRepository.findByEmail(email);
        if (tenantOpt.isPresent()) {
            Tenant tenant = tenantOpt.get();
            LoggedUser user = new LoggedUser();
            user.setId(tenant.getId());
            user.setFullName(tenant.getFullName());
            user.setRole(tenant.getRole());
            return user;
        }

        // If email not found in any table
        throw new Exception("User not found for token");
    }
}
