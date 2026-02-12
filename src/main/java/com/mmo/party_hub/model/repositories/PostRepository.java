package com.mmo.party_hub.model.repositories;

import com.mmo.party_hub.model.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // Busca posts de um personagem específico para o perfil dele
    List<Post> findByCharacterIdOrderByCreatedAtDesc(Long characterId);
}