package com.mmo.party_hub.model.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mmo.party_hub.model.entities.Comment;
import com.mmo.party_hub.model.entities.CommentLike;
import com.mmo.party_hub.model.entities.GameCharacter;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {
    Optional<CommentLike> findByAuthorCharacterAndComment(GameCharacter character, Comment comment);
}