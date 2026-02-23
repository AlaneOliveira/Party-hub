package com.mmo.party_hub.model.repositories;

import com.mmo.party_hub.model.entities.GameCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface GameCharacterRepository extends JpaRepository<GameCharacter, Long> {
    List<GameCharacter> findByGameTitle(String gameTitle);
    
    // Para listar os personagens de um Gamer específico
    List<GameCharacter> findByGamerId(Long gamerId);

    List<GameCharacter> findByNameContainingIgnoreCaseAndGameTitle(String name, String gameTitle);

    @Query("SELECT c FROM GameCharacter c WHERE c.gameTitle = :gameTitle " +
       "AND c.id != :viewerId " +
       "AND c.id NOT IN (SELECT f.following.id FROM Follower f WHERE f.follower.id = :viewerId)")
    List<GameCharacter> findSuggestionsByGame(
        @Param("gameTitle") String gameTitle, 
        @Param("viewerId") Long viewerId, 
        Pageable pageable
    );
}