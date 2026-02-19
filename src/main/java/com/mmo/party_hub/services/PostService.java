package com.mmo.party_hub.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmo.party_hub.dto.NewPostDTO;
import com.mmo.party_hub.dto.PostDTO;
import com.mmo.party_hub.dto.PublicPostDTO;
import com.mmo.party_hub.model.entities.Bet;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.entities.Post;
import com.mmo.party_hub.model.repositories.BetRepository;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.model.repositories.PostRepository;
import com.mmo.party_hub.security.JwtUtils;

import jakarta.transaction.Transactional;

@Service
public class PostService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private GamerRepository gamerRepo;

    @Autowired
    private BetRepository betRepo;

    @Transactional
    public ResponseEntity<?> save(NewPostDTO dto){

        if(dto.getBetValue() < 10 || dto.getContent().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        // 1. Converte o ID do Token para Long (Tipo do seu Repository)
        Long idGamer = Long.valueOf(jwtUtils.getAuthorizedId());
        
        // 2. Busca o Gamer uma ÚNICA vez
        Gamer gamer = gamerRepo.findById(idGamer)
                .orElseThrow(() -> new RuntimeException("Gamer não encontrado"));

        Post post = new Post();
        post.setContent(dto.getContent());
        post.setAuthor(gamer);
        post = postRepository.save(post);

        Bet bet = new Bet();
        bet.setAnswer(dto.isBetAnswer());
        bet.setValue(dto.getBetValue() - (dto.getBetValue() / 10));
        bet.setPost(post); 
        bet.setGamer(gamer);

        betRepo.save(bet);

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> getAuthorizedPosts(){
        // 3. Converte para Long aqui também para evitar o erro de Incompatible Types
        Long idGamer = Long.valueOf(jwtUtils.getAuthorizedId());
        Gamer gamer = gamerRepo.findById(idGamer).orElseThrow();
        
        String gamerEmail = gamer.getEmail(); 

        List<Post> posts = this.postRepository.findByAuthorId(idGamer)
                            .orElseThrow(() -> new RuntimeException("Nenhum post encontrado"));

        List<PostDTO> dtos = posts.stream().map(p -> {
            PostDTO dto = new PostDTO(p.getId(), p.getContent(), "Desc", "Cat", p.getDate());
            Double totalPot = betRepo.sumBetValue(p.getId());
            dto.setPot(totalPot != null ? totalPot : 0.0);
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
    public ResponseEntity<?> getPublicAvailablePosts() {
    List<PublicPostDTO> posts = postRepository.findAvailable().orElseThrow();
    return ResponseEntity.ok(posts);
}
}    