package com.mmo.party_hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmo.party_hub.dto.PostLikeDTO;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.entities.Post;
import com.mmo.party_hub.model.entities.PostLike;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.model.repositories.PostLikeRepository;
import com.mmo.party_hub.model.repositories.PostRepository;
import com.mmo.party_hub.security.JwtUtils;
import java.util.Optional;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository likeRepo;

    @Autowired
    private GamerRepository gamerRepo;

    @Autowired
    private PostRepository postRepo;

    @Autowired
    private JwtUtils jwtUtils;

    public ResponseEntity<?> like(PostLikeDTO dto) {
    long gamerId = Long.parseLong(jwtUtils.getAuthorizedId());

    Gamer gamer = gamerRepo.findById(gamerId).orElseThrow(() -> new RuntimeException("Gamer não encontrado"));
    Post post = postRepo.findById(dto.getPostId()).orElseThrow(() -> new RuntimeException("Post não encontrado"));

    // VERIFICAÇÃO: O usuário já curtiu este post?
    Optional<PostLike> existingLike = likeRepo.findByAuthorAndComment(gamer, post); 
        if (existingLike.isPresent()) {
            // Se já existe, REMOVE o like (Unlike)
            likeRepo.delete(existingLike.get());
            return ResponseEntity.ok("Like removido!");
        } else {
            // Se não existe, ADICIONA o like
            PostLike like = new PostLike();
            like.setAuthor(gamer);
            like.setComment(post); // 'comment' é o nome na sua entidade
            like.setDate(System.currentTimeMillis());

            likeRepo.save(like);
            return ResponseEntity.ok("Like adicionado!");
        }
    }
}