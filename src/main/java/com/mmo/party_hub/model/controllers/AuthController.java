package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmo.party_hub.dto.LoginDTO;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.services.AuthService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authS;

    @PostMapping
    public ResponseEntity<?> postNewGamer(@RequestBody Gamer gamer) {
        return this.authS.newGamer(gamer); 
    }

    @PostMapping("/login")
    public ResponseEntity<?> postMethodName(@RequestBody LoginDTO login) {
        return this.authS.login(login);
    }

}