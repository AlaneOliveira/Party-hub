package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable; 
import com.mmo.party_hub.services.PostService;
import com.mmo.party_hub.dto.NewPostDTO;
import com.mmo.party_hub.model.entities.Post; 
import com.mmo.party_hub.model.repositories.PostRepository;
import com.mmo.party_hub.security.JwtUtils;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postS;
 
    @Autowired 
    private PostRepository postRepository;

    @Autowired 
    private JwtUtils jwtUtils;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody NewPostDTO post) {
        return postS.save(post);
    }
    
    @GetMapping("/character/{id}")
    public ResponseEntity<?> getCharacterPosts(@PathVariable Integer id, @RequestParam Integer viewerId) {
        return this.postS.getCharacterPosts(id, viewerId);
    }

    @GetMapping
    public ResponseEntity<?> getPosts(
        @RequestParam int characterId, 
        @RequestParam(required = false, defaultValue = "false") boolean sameGame) { 
        
        if (sameGame) {
            return this.postS.getSameGameFeed(characterId);
        }
        return this.postS.getGlobalFeed(characterId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Integer id) {
        Post post = postRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        // Validação de segurança: apenas o dono do gamer que criou o post pode deletar
        Long loggedGamerId = Long.valueOf(jwtUtils.getAuthorizedId());
        if (!post.getCharacter().getGamer().getId().equals(loggedGamerId)) {
            return ResponseEntity.status(403).body("Acesso negado");
        }

        postRepository.delete(post);
        return ResponseEntity.ok().build();
    }
}