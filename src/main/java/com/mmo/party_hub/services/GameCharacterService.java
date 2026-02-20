package com.mmo.party_hub.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mmo.party_hub.dto.GameCharacterDTO;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.entities.Photo;
import com.mmo.party_hub.model.repositories.GameCharacterRepository;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.model.repositories.PhotoRepository;
import com.mmo.party_hub.security.JwtUtils;

@Service
public class GameCharacterService {

    @Autowired
    private GameCharacterRepository characterRepository;

    @Autowired
    private GamerRepository gamerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PhotoRepository photoRepository;

    public ResponseEntity<?> newGameCharacter(GameCharacterDTO dto) {
        try {
            // 1. Pegamos o ID do token como String e convertemos para Long
            Long idNumerico = Long.valueOf(jwtUtils.getAuthorizedId()); 

            // 2. Buscamos usando o número
            Gamer gamer = gamerRepository.findById(idNumerico)
                .orElseThrow(() -> new RuntimeException("Gamer not found"));

            GameCharacter character = new GameCharacter();
            character.setName(dto.getName());
            character.setClazz(dto.getClazz());
            character.setGameTitle(dto.getGameTitle());
            character.setLevel(1);
            character.setGamer(gamer);
            
            GameCharacter salvo = characterRepository.save(character);
            return ResponseEntity.ok(new GameCharacterDTO(salvo)); 
            
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro: " + e.getMessage());
        }
    }
    
    public ResponseEntity<?> uploadPhoto(Long charId, MultipartFile file) {
    try {
        GameCharacter character = characterRepository.findById(charId)
                .orElseThrow(() -> new RuntimeException("Game character not found"));

        // Busca foto existente ou cria nova
        Photo p = photoRepository.findByCharacterId(charId).orElse(new Photo());
        p.setContent(file.getBytes());
        p.setExtension(file.getContentType().split("/")[1]);
        p.setLength((int) file.getSize());
        p.setCharacter(character);

        photoRepository.save(p);
        
        character.setImageUrl("/photos/show/" + charId); 
        characterRepository.save(character);

        return ResponseEntity.ok().build();
    } catch (Exception e) {
        return ResponseEntity.internalServerError().body("Error processing the photo: " + e.getMessage());
    }
}

    public List<GameCharacter> findByGamerId(Long gamerId) {
        return characterRepository.findByGamerId(gamerId);
    }
}