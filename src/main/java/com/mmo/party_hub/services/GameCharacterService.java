package com.mmo.party_hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmo.party_hub.dto.GamerCharacterDTO;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.repositories.GameCharacterRepository;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.security.JwtUtils;

@Service
public class GameCharacterService {

    @Autowired
    private GameCharacterRepository characterRepository;

    @Autowired
    private GamerRepository gamerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<?> newGameCharacter(GamerCharacterDTO dto) {
    try {
        // 1. Pegamos o ID do token como String
        String idDoToken = jwtUtils.getAuthorizedId(); 

        // 2. Convertemos para Long (para bater com o tipo do banco)
        Long idNumerico = Long.valueOf(idDoToken); 

        // 3. Buscamos usando o número
        Gamer gamer = gamerRepository.findById(idNumerico)
            .orElseThrow(() -> new RuntimeException("Gamer não encontrado"));

        GameCharacter character = new GameCharacter();
        character.setName(dto.getName());
        character.setClazz(dto.getClazz());
        character.setGameTitle(dto.getGameTitle());
        character.setImageUrl(dto.getImageUrl());
        character.setLevel(1);
        
        character.setGamer(gamer);
        characterRepository.save(character);

        return ResponseEntity.ok("Personagem " + dto.getName() + " criado com sucesso!");
    } catch (Exception e) {
        return ResponseEntity.status(400).body("Erro: " + e.getMessage());
    }
}
}