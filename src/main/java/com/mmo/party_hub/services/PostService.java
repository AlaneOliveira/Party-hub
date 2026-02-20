package com.mmo.party_hub.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmo.party_hub.dto.NewPostDTO;
import com.mmo.party_hub.dto.PostDTO;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.entities.Post;
import com.mmo.party_hub.model.repositories.GameCharacterRepository;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.model.repositories.PostLikeRepository;
import com.mmo.party_hub.model.repositories.PostRepository;
import com.mmo.party_hub.security.JwtUtils;

@Service
public class PostService {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PostRepository postRepository;

    @Autowired // Mudei para gamerRepo para bater com o resto do código
    private GamerRepository gamerRepo;

    @Autowired
    private GameCharacterRepository characterRepository;

    @Autowired
    private PostLikeRepository postLikeRepository;

    public ResponseEntity<?> save(NewPostDTO dto) {
        try {
            // 1. Agora usando a variável correta e injetada
            GameCharacter character = characterRepository.findById(dto.getCharacterId())
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

            Post post = new Post();
            post.setContent(dto.getContent());
            post.setDescription(dto.getDescription());
            post.setCategory(dto.getCategory());
            post.setCharacter(character); 
            
            // 2. Usando o nome correto: gamerRepo
            String idDoToken = jwtUtils.getAuthorizedId();
            Gamer author = gamerRepo.findById(Long.valueOf(idDoToken))
                .orElseThrow(() -> new RuntimeException("Autor não encontrado"));
            
            post.setAuthor(author);
            postRepository.save(post);
            return ResponseEntity.ok("Post criado!");
        } catch (Exception e) {
            // Se cair aqui, o Postman recebe 400 com a mensagem do erro
            return ResponseEntity.status(400).body("Erro: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getAuthorizedPosts() {
        // 3. Converte para Long aqui também para evitar o erro de Incompatible Types
        Long idGamer = Long.valueOf(jwtUtils.getAuthorizedId());
        List<Post> posts = this.postRepository.findByAuthorId(idGamer)
                .orElseThrow(() -> new RuntimeException("Nenhum post encontrado"));

        List<PostDTO> dtos = posts.stream().map(p -> {
            PostDTO dto = new PostDTO(p.getId(), p.getContent(), p.getDescription(), p.getCategory(), p.getDate());
            // Buscando a contagem de curtidas no PostLikeRepository
            dto.setLikes((int) postLikeRepository.countByCommentId(p.getId()));
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> getGlobalFeed() {
        List<Post> posts = this.postRepository.findAllByOrderByDateDesc();

        List<PostDTO> dtos = posts.stream().map(p -> {
            // Criando o DTO com os dados da entidade
            PostDTO dto = new PostDTO(p.getId(), p.getContent(), p.getDescription(), p.getCategory(), p.getDate());
            // Buscando a contagem de curtidas no PostLikeRepository
            dto.setLikes((int) postLikeRepository.countByCommentId(p.getId()));
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}