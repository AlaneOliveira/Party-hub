package com.mmo.party_hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmo.party_hub.dto.PostLikeDTO;
import com.mmo.party_hub.model.entities.GameCharacter; 
import com.mmo.party_hub.model.entities.Post;
import com.mmo.party_hub.model.entities.PostLike; 
import com.mmo.party_hub.model.repositories.PostLikeRepository;
import com.mmo.party_hub.model.repositories.PostRepository; 
import com.mmo.party_hub.model.repositories.GameCharacterRepository;
import java.util.Optional;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository likeRepo;

    @Autowired
    private GameCharacterRepository characterRepository;

    @Autowired
    private PostRepository postRepo;

    public ResponseEntity<?> like(PostLikeDTO dto) { 
        // 1. Buscamos o Personagem pelo ID dele (que vem do DTO como Long)
        // Se o seu DTO characterId for int, use (long) dto.getCharacterId()
        GameCharacter character = characterRepository.findById((long) dto.getCharacterId())
            .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));
        
        // 2. Buscamos o Post
        Post post = postRepo.findById(dto.getPostId())
            .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        // 3. Verificamos se ESSE personagem já curtiu ESSE post
        Optional<PostLike> existingLike = likeRepo.findByAuthorCharacterAndPost(character, post);

        if (existingLike.isPresent()) {
            likeRepo.delete(existingLike.get());
            
            // Lógica de decremento
            post.setLikesCount(Math.max(0, post.getLikesCount() - 1));
            postRepo.save(post);
            
            return ResponseEntity.ok("Like removido!");
        } else {
            PostLike like = new PostLike();
            like.setAuthorCharacter(character);
            like.setPost(post);
            like.setDate(System.currentTimeMillis());
            
            likeRepo.save(like);
            
            // Lógica de incremento
            post.setLikesCount(post.getLikesCount() + 1);
            postRepo.save(post);
            
            return ResponseEntity.ok("Like adicionado!");
        }
    } 
} 