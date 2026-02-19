package com.mmo.party_hub.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired // Injeção automática pelo Spring
    private JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .cors(Customizer.withDefaults()) 
            .authorizeHttpRequests(auth -> auth
                // Mantendo a lógica de permissões do seu modelo
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/gamer/**").hasAuthority("GAMER") // Troque hasRole por hasAuthority
                .requestMatchers("/comment/**").hasAuthority("GAMER") // No lugar de question
                .requestMatchers("/bet/**").hasAuthority("GAMER")
                .requestMatchers("/post/**").hasAuthority("GAMER")
                .requestMatchers("/public/**").permitAll()
                
                .anyRequest().authenticated()
            )
           
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}