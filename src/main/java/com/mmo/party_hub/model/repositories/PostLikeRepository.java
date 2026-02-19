package com.mmo.party_hub.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmo.party_hub.model.entities.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // Como na entidade PostLike o campo é 'comment', o método deve ser:
    long countByCommentId(Integer commentId); 
}