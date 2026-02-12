package com.mmo.party_hub.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mmo.party_hub.model.entities.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    long countByPostId(Long postId); // Para mostrar o total de curtidas
}

