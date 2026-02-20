package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mmo.party_hub.dto.PostLikeDTO;
import com.mmo.party_hub.services.PostLikeService;

@RestController
@RequestMapping("/like")
public class PostLikeController {

    @Autowired
    private PostLikeService likeService;

    @PostMapping
    public ResponseEntity<?> like(@RequestBody PostLikeDTO dto) {
        return likeService.like(dto);
    }
}