package com.mmo.party_hub.model.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mmo.party_hub.dto.PublicPostDTO;
import com.mmo.party_hub.model.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    
    // ATUALIZADO: Agora busca pelo ID numérico do autor
    Optional<List<Post>> findByAuthorId(Long authorId);

    // Mantendo sua query de feed que já corrigimos com o 'count'
    @Query("select new com.mmo.party_hub.dto.PublicPostDTO(p, (select count(pl) from PostLike pl where pl.comment.id = p.id)) from Post p")
    public Optional<List<PublicPostDTO>> findAvailable();
}
