package com.mmo.party_hub.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.mmo.party_hub.model.entities.Bet;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.repositories.BetRepository;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.security.JwtUtils;

    @Component
public class BetService {

    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private BetRepository betRepo;
    
    @Autowired
    private GamerRepository gamerRepo; // Você vai precisar dele aqui

    public ResponseEntity<?> save(Bet b){
        // 1. Pega o ID que agora vem como String do Token e converte para Long
        Long idGamer = Long.valueOf(this.jwtUtils.getAuthorizedId());
        

        // 2. Busca o Gamer completo no banco (evita erros de transiente no Hibernate)
        Gamer g = gamerRepo.findById(idGamer)
                  .orElseThrow(() -> new RuntimeException("Gamer não encontrado"));
        
        // 3. Vincula o Gamer real à aposta
        b.setGamer(g);

        // Lógica da taxa de 10%
        b.setValue(b.getValue() - (b.getValue() / 10));

        this.betRepo.save(b);

        return ResponseEntity.ok().build();
    }
}