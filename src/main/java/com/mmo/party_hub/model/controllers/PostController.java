package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable; 
import com.mmo.party_hub.services.PostService;
import com.mmo.party_hub.dto.NewPostDTO; 

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postS;
 
    @PostMapping
    public ResponseEntity<?> post(@RequestBody NewPostDTO post) {
        return postS.save(post);
    }

    // Alterado para retornar o Feed Global (todos os posts de todos os personagens)
    @GetMapping
    public ResponseEntity<?> getPosts(@RequestParam int characterId) { // Recebe o ID via URL
        return this.postS.getGlobalFeed(characterId);
    }

    @GetMapping("/character/{id}")
    public ResponseEntity<?> getCharacterPosts(@PathVariable Integer id) {
        return this.postS.getCharacterPosts(id);
    }
}