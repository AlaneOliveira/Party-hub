package com.mmo.party_hub.model.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mmo.party_hub.model.entities.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {
    
    Optional<Photo> findByGId(Long gamerId);
    Optional<Photo> findByCharacterId(Long characterId);
    
}