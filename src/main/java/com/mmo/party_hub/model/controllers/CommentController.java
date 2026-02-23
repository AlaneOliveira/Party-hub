package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.mmo.party_hub.model.entities.Comment;
import com.mmo.party_hub.dto.CommentDTO;
import com.mmo.party_hub.dto.CommentLikeDTO;
import com.mmo.party_hub.model.repositories.CommentRepository;
import com.mmo.party_hub.security.JwtUtils;
import com.mmo.party_hub.services.CommentService;
import com.mmo.party_hub.services.CommentLikeService; 

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentS;
    
    @Autowired
    private CommentLikeService commentLikeS; 

    @Autowired 
    private CommentRepository commentRepository;

    @Autowired 
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDTO dto) {
        return commentS.createComment(dto);
    }

    @PostMapping("/like")
    public ResponseEntity<?> likeComment(@RequestBody CommentLikeDTO dto) {
        return commentLikeS.toggleLike(dto.getCommentId(), dto.getCharacterId());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        Comment comment = commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comentário não encontrado"));

        Long loggedGamerId = Long.valueOf(jwtUtils.getAuthorizedId());
        if (!comment.getCharacter().getGamer().getId().equals(loggedGamerId)) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        commentRepository.delete(comment);
        return ResponseEntity.ok().build();
    }
}