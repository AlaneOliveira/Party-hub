package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mmo.party_hub.dto.GamerCharacterDTO; // IMPORTANTE
import com.mmo.party_hub.dto.NewPasswordDTO;
import com.mmo.party_hub.services.GameCharacterService; // IMPORTANTE
import com.mmo.party_hub.services.GamerService;


@RestController
// usuario info
@RequestMapping("/gamer")
public class GamerController {
    @Autowired
    private GamerService gamerS;

    // ESTA LINHA ABAIXO É A QUE ESTAVA FALTANDO
    @Autowired
    private GameCharacterService characterService;

    @GetMapping
    public ResponseEntity<?> getGamer() {
        return this.gamerS.getGamer();
    }
    // usuario tem senha
    @PatchMapping("/password")
    public ResponseEntity<?> newPassword (@RequestBody NewPasswordDTO newPass){
        return this.gamerS.updatePassword(newPass);
    }
    // usuario tem foto
    @PatchMapping("/perfil")
    public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file){
        return this.gamerS.uploadPhoto(file);
    }
    @PostMapping("/character")
    public ResponseEntity<?> addCaracter(@RequestBody GamerCharacterDTO dto){
        return this.characterService.newGameCharacter(dto);
    }
}