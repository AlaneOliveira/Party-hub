package com.mmo.party_hub.model.controllers;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mmo.party_hub.dto.GameCharacterDTO;
import com.mmo.party_hub.dto.NewPasswordDTO;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.services.GameCharacterService;
import com.mmo.party_hub.services.GamerService;
import com.mmo.party_hub.security.JwtUtils;
import java.util.List;
import java.util.Map; 

@RestController
@RequestMapping("/gamer")
public class GamerController {

    @Autowired
    private GamerService gamerS;

    @Autowired
    private GameCharacterService characterService;

    @Autowired
    private JwtUtils jwtUtils;


    @GetMapping
    public ResponseEntity<?> getGamer() {
        return this.gamerS.getGamer();
    }

    @PatchMapping("/password")
    public ResponseEntity<?> newPassword (@RequestBody NewPasswordDTO newPass){
        return this.gamerS.updatePassword(newPass);
    }

 
    @PatchMapping("/perfil")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file){
        return this.gamerS.uploadPhoto(file);
    }

    @PostMapping("/character")
    public ResponseEntity<?> addCaracter(@RequestBody GameCharacterDTO dto){
        return this.characterService.newGameCharacter(dto);
    }

    @GetMapping("/character/me")
    public ResponseEntity<List<GameCharacterDTO>> getMyCharacters() {

        Long gamerId = Long.valueOf(jwtUtils.getAuthorizedId());

        List<GameCharacter> list = characterService.findByGamerId(gamerId);
        
        List<GameCharacterDTO> dtos = list.stream()
            .map(GameCharacterDTO::new)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/character/{id}/photo") // nova rota para upload de foto do personagem, usando o ID do personagem na URL
    public ResponseEntity<?> uploadCharacterPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        return characterService.uploadPhoto(id, file);
    }

    @PostMapping("/character/follow")
    public ResponseEntity<?> follow(@RequestBody Map<String, Long> payload) {
        return characterService.toggleFollow(payload.get("followerId"), payload.get("followingId"));
    }

    @GetMapping("/character/{id}")
    public ResponseEntity<?> getCharacterProfile(@PathVariable Long id) {
        GameCharacter character = characterService.findById(id);
        GameCharacterDTO dto = new GameCharacterDTO(character);
        return ResponseEntity.ok(dto);
    }

}