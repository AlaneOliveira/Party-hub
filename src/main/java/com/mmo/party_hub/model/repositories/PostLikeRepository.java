package com.mmo.party_hub.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmo.party_hub.model.entities.GameCharacter; 
import com.mmo.party_hub.model.entities.Post;
import com.mmo.party_hub.model.entities.PostLike;
import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> { 
    // Busca um like específico de um autor para um post (usando os nomes dos campos da sua Entidade)
    Optional<PostLike> findByAuthorCharacterAndPost(GameCharacter authorCharacter, Post post);
}