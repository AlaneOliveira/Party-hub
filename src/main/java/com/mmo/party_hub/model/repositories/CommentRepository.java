package com.mmo.party_hub.model.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmo.party_hub.model.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    List<Comment> findByPostId(int postId); // busca comentários de um post
}