package com.mmo.party_hub.model.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mmo.party_hub.model.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<List<Post>> findByAuthorId(Long authorId);
} 