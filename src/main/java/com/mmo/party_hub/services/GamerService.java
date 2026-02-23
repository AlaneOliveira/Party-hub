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

    public ResponseEntity<?> getGamer() {
    // Busca o ID do usuário logado via Token
        Long gamerId = Long.valueOf(jwtUtils.getAuthorizedId());
        Gamer gamer = gamerRepo.findById(gamerId).orElse(null);
        
        if (gamer == null) return ResponseEntity.status(404).body("Usuário não encontrado");
        
        // Retorne o objeto Gamer (o Jackson transformará em JSON com o campo 'name')
        return ResponseEntity.ok(gamer);
    }

    public ResponseEntity<?> updatePassword(NewPasswordDTO passDto) {
        Long id = Long.valueOf(jwtUtils.getAuthorizedId());
        Gamer u = gamerRepo.findById(id)
                             .orElseThrow(() -> new RuntimeException("Gamer not found"));

        if (encoder.matches(passDto.getOldPassword(), u.getPassword())) {
            u.setPassword(encoder.encode(passDto.getNewPassword()));
            gamerRepo.save(u);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Incorrect old password.");
    }
}
