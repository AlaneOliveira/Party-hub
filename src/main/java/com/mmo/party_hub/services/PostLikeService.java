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

        Gamer gamer = gamerRepo.findById(gamerId).orElse(null);
        if (gamer == null) return ResponseEntity.status(404).body("Gamer not found.");

        Post post = postRepo.findById((Integer) dto.getPostId()).orElse(null);
        if (post == null) return ResponseEntity.status(404).body("Not found.");

        PostLike like = new PostLike();
        like.setAuthor(gamer);
        like.setComment(post); // 'comment' é o nome do campo na entidade
        like.setDate(System.currentTimeMillis());

        likeRepo.save(like);
        return ResponseEntity.ok("Like add!");
    }
}