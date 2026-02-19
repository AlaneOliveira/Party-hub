package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mmo.party_hub.dto.CommentDTO;
import com.mmo.party_hub.services.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentS;

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentDTO dto) {
        return commentS.createComment(dto);
    }
}