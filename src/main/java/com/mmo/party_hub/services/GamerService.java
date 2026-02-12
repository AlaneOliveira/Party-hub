package com.mmo.party_hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mmo.party_hub.dto.NewPasswordDTO;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.security.JwtUtils;

@Component
public class GamerService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private GamerRepository gamerRepo;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> getUser() {

        String email = jwtUtils.getAuthorizedId();
        Gamer u = gamerRepo.findByEmail(email)
                           .orElseThrow(() -> new RuntimeException("User not found"));

        u.setPassword(null); 

        return ResponseEntity.ok(u);
    }

    public ResponseEntity<?> updatePassword(NewPasswordDTO passDto){ 

        String email = this.jwtUtils.getAuthorizedId();
        Gamer u = this.gamerRepo.findByEmail(email)
                           .orElseThrow(() -> new RuntimeException("User not found"));

        if(encoder.matches(passDto.getOldPassword(), u.getPassword())){
            u.setPassword(encoder.encode(passDto.getNewPassword()));
            this.gamerRepo.save(u);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.badRequest().body("Senha antiga incorreta");
    }

}