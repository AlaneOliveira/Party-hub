package com.mmo.party_hub.model.repositories;

import com.mmo.party_hub.model.entities.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameCharacterRepository extends JpaRepository<GameCharacter, Long> {
    List<GameCharacter> findByGameTitle(String gameTitle);
    
    // Para listar os personagens de um Gamer específico
    List<GameCharacter> findByGamerId(Long gamerId);
}