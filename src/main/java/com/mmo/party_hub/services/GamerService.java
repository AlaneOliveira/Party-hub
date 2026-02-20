package com.mmo.party_hub.services;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.mmo.party_hub.dto.NewPasswordDTO;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.entities.Photo;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.model.repositories.PhotoRepository;
import com.mmo.party_hub.security.JwtUtils;

@Component
public class GamerService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private GamerRepository gamerRepo;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private PhotoRepository photoRepository;

    public ResponseEntity<?> getGamer() {
        // 1. Pegamos o ID (que vem do Subject do Token)
        Long id = Long.valueOf(jwtUtils.getAuthorizedId()); 
        
        // 2. Buscamos pelo ID numérico
        Gamer u = gamerRepo.findById(id)
                             .orElseThrow(() -> new RuntimeException("Gamer não encontrado"));
        u.setPassword(null); 
        return ResponseEntity.ok(u);
    }

    public ResponseEntity<?> updatePassword(NewPasswordDTO passDto) {
        Long id = Long.valueOf(jwtUtils.getAuthorizedId());
        Gamer u = gamerRepo.findById(id)
                             .orElseThrow(() -> new RuntimeException("Gamer não encontrado"));

        if (encoder.matches(passDto.getOldPassword(), u.getPassword())) {
            u.setPassword(encoder.encode(passDto.getNewPassword()));
            gamerRepo.save(u);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().body("Senha antiga incorreta");
    }

    public ResponseEntity<?> uploadPhoto(MultipartFile file) {
        try {
            Long id = Long.valueOf(jwtUtils.getAuthorizedId());
            Gamer gamer = this.gamerRepo.findById(id)
                                 .orElseThrow(() -> new RuntimeException("Gamer não encontrado"));

            Photo p = new Photo();
            p.setContent(file.getBytes());
            p.setExtension(file.getContentType().split("/")[1]);
            p.setLength((int) file.getSize());
            p.setG(gamer);

            photoRepository.save(p);
            return ResponseEntity.ok().build();
            
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Erro ao processar a imagem");
        }
    }

    public ResponseEntity<?> getPerfil() {
        Long id = Long.valueOf(this.jwtUtils.getAuthorizedId());
        Optional<Photo> photoOpt = this.photoRepository.findByGId(id);

        if (photoOpt.isPresent()) {
            Photo p = photoOpt.get();
            p.setG(null);
            return ResponseEntity.ok(p);
        }

        return ResponseEntity.notFound().build();
    }
}
