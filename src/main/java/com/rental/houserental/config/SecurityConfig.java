package com.rental.houserental.config;

import com.rental.houserental.security.JwtAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(STATELESS)
                )
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**", "/api/properties/public/**").permitAll()
                                .requestMatchers("/api/owner/**").hasRole("OWNER")
                                // Admin routes (ADMIN role required)
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                       // .anyRequest().permitAll()
                );
        http.addFilterBefore(jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class);





        return http.build();
    }

}
