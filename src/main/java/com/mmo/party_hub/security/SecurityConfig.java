package com.mmo.party_hub.security;

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

    @Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf.disable()) 
        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(Customizer.withDefaults()) 
        .authorizeHttpRequests(auth -> auth
            // LIBERA AS ROTAS DE LOGIN E CADASTRO
            .requestMatchers("/auth/**").permitAll() 
            // TODO: Ajustar as urls de acordo com as necessidades do projeto
                /*
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/user/**").hasRole("USER")
                .requestMatchers("/question/**").hasRole("USER")
                .requestMatchers("/bet/**").hasRole("USER")
                .requestMatchers("/public/**").permitAll()
                 */

            // O RESTO EXIGE TOKEN (COMO O GETUSER)
            .anyRequest().authenticated()
        )
        .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }
}