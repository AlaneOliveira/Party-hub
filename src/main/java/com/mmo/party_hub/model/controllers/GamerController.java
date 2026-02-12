package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmo.party_hub.dto.NewPasswordDTO;
import com.mmo.party_hub.services.GamerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/gamer")
public class GamerController {
    @Autowired
    private GamerService gamerS;

    @GetMapping
    public ResponseEntity<?> getUser() {
        return this.gamerS.getUser();
    }

    @PatchMapping("/password")
    public ResponseEntity<?> newPassword (@RequestBody NewPasswordDTO newPass){
        return this.gamerS.updatePassword(newPass);
    }
    
}