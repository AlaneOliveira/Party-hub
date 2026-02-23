package com.mmo.party_hub.model.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.mmo.party_hub.model.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    Optional<List<Post>> findByAuthorId(Long authorId);
    List<Post> findAllByOrderByDateDesc();
    List<Post> findByCharacterIdOrderByDateDesc(Integer characterId);
    List<Post> findByCharacterGameTitleOrderByDateDesc(String gameTitle);

    @Query("SELECT p FROM Post p WHERE p.character.gameTitle = :gameTitle " +
            "AND (p.character.id = :viewerId " + // INCLUI SEUS PRÓPRIOS POSTS
            "OR p.character.id IN (SELECT f.following.id FROM Follower f WHERE f.follower.id = :viewerId)) " +
            "ORDER BY p.date DESC")
        List<Post> findPostsByUniverseAndFollowing(@Param("gameTitle") String gameTitle, @Param("viewerId") Long viewerId);
}