package com.mmo.party_hub.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.mmo.party_hub.model.entities.Comment;
import com.mmo.party_hub.model.entities.CommentLike;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.model.repositories.CommentLikeRepository;
import com.mmo.party_hub.model.repositories.CommentRepository;
import com.mmo.party_hub.model.repositories.GameCharacterRepository; 

@Service
public class CommentLikeService {
    @Autowired private CommentLikeRepository likeRepo;
    @Autowired private CommentRepository commentRepo;
    @Autowired private GameCharacterRepository characterRepo;

    public ResponseEntity<?> toggleLike(int commentId, int characterId) {
        GameCharacter character = characterRepo.findById((long) characterId).orElseThrow();
        Comment comment = commentRepo.findById(commentId).orElseThrow();

        Optional<CommentLike> existing = likeRepo.findByAuthorCharacterAndComment(character, comment);

        if (existing.isPresent()) {
            likeRepo.delete(existing.get());
            comment.setLikesCount(Math.max(0, comment.getLikesCount() - 1));
            commentRepo.save(comment);
            return ResponseEntity.ok("Like removido");
        } else {
            CommentLike like = new CommentLike();
            like.setAuthorCharacter(character);
            like.setComment(comment);
            like.setDate(System.currentTimeMillis());
            likeRepo.save(like);

            comment.setLikesCount(comment.getLikesCount() + 1);
            commentRepo.save(comment);
            return ResponseEntity.ok("Like adicionado");
        }
    }
}
