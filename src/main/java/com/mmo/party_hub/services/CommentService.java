package com.mmo.party_hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmo.party_hub.dto.CommentDTO;
import com.mmo.party_hub.model.entities.Comment;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.entities.Post;
import com.mmo.party_hub.model.repositories.CommentRepository;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.model.repositories.PostRepository;
import com.mmo.party_hub.security.JwtUtils;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;

    @Autowired
    private GamerRepository gamerRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<?> createComment(CommentDTO dto) {
        // Pega o ID do gamer logado pelo token
        long gamerId = Long.parseLong(jwtUtils.getAuthorizedId());

        Gamer gamer = gamerRepo.findById(gamerId).orElse(null);
        if (gamer == null) return ResponseEntity.status(404).body("Gamer não encontrado.");

        Post post = postRepo.findById(dto.getPostId()).orElse(null);
        if (post == null) return ResponseEntity.status(404).body("Post não encontrado.");

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setDate(System.currentTimeMillis());
        comment.setGamer(gamer);
        comment.setPost(post);

        commentRepo.save(comment);
        return ResponseEntity.ok("Comentário criado com sucesso!");
    }
}