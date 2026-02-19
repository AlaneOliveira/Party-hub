package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mmo.party_hub.dto.NewPostDTO;
import com.mmo.party_hub.services.PostService;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postS;

    @PostMapping
    public ResponseEntity<?> post(@RequestBody NewPostDTO post) {
        return postS.save(post);
    }

    @GetMapping
    public ResponseEntity<?> getPosts() {
        return this.postS.getAuthorizedPosts();
    }
}