package com.mmo.party_hub.model.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mmo.party_hub.model.entities.Bet;
import com.mmo.party_hub.services.BetService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/bet")
public class BetController {

    @Autowired
    private BetService betS;

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Bet bet) {
        return this.betS.save(bet);
    }
    

}
