package com.mmo.party_hub.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.mmo.party_hub.dto.LoginDTO;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.security.JwtUtils;

@Component
public class AuthService {

    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private GamerRepository gamerRepo;
    @Autowired
    private PasswordEncoder encoder;

    public ResponseEntity<?> newGamer(Gamer gamer){

        gamer.setPassword(this.encoder.encode(gamer.getPassword()));

        this.gamerRepo.save(gamer);
        
        return ResponseEntity.ok("Success!");

    }

    public ResponseEntity<?> login(LoginDTO login){
        
        Optional<Gamer> gamerOpt = this.gamerRepo.findByEmail(login.getLogin());

        if(gamerOpt.isPresent()){

            Gamer gamer = gamerOpt.get();

            if(this.encoder.matches(login.getPassword(), gamer.getPassword())){
                return ResponseEntity.ok(this.jwtUtils
                    .generateToken(gamer.getEmail(), "GAMER"));
            }

        }

        return ResponseEntity.badRequest().body("Invalid Credentials");
    }

}