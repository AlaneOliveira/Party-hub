package com.mmo.party_hub.model.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmo.party_hub.services.PostService;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private PostService commentS;

    @GetMapping("/post")
    public ResponseEntity<?> availableComments(){
        return this.commentS.getPublicAvailablePosts();
    }
}

