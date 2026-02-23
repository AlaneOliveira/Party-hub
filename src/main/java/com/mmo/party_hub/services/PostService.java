package com.mmo.party_hub.services;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmo.party_hub.dto.CommentDTO;
import com.mmo.party_hub.dto.NewPostDTO;
import com.mmo.party_hub.dto.PostDTO;
import com.mmo.party_hub.model.entities.Comment;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.entities.Post;
import com.mmo.party_hub.model.repositories.GameCharacterRepository;
import com.mmo.party_hub.model.repositories.GamerRepository; 
import com.mmo.party_hub.model.repositories.PostRepository;
import com.mmo.party_hub.security.JwtUtils;
import com.mmo.party_hub.model.repositories.PostLikeRepository; 

@Service
public class PostService {
    @Autowired private JwtUtils jwtUtils;
    @Autowired private PostRepository postRepository;
    @Autowired private GamerRepository gamerRepo;
    @Autowired private GameCharacterRepository characterRepository; 
    @Autowired private PostLikeRepository postLikeRepository; 

    public ResponseEntity<?> save(NewPostDTO dto) {
        try {
            GameCharacter character = characterRepository.findById(dto.getCharacterId())
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

            Post post = new Post();
            post.setTitle(dto.getTitle());
            post.setContent(dto.getContent());
            post.setDate(System.currentTimeMillis());
            post.setCharacter(character); 
            
            String idDoToken = jwtUtils.getAuthorizedId();
            Gamer author = gamerRepo.findById(Long.valueOf(idDoToken)).orElseThrow();
            
            post.setAuthor(author);
            postRepository.save(post);
            return ResponseEntity.ok("Post criado!");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getGlobalFeed(int characterId) {
        GameCharacter viewer = characterRepository.findById((long) characterId)
            .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        // Busca posts: Mesmo Universo + Personagens que o viewer segue
        List<Post> posts = postRepository.findPostsByUniverseAndFollowing(viewer.getGameTitle(), (long) characterId);

        List<PostDTO> dtos = posts.stream().map(p -> {
            PostDTO dto = new PostDTO(
                p.getId(), p.getTitle(), p.getContent(), p.getDate(), 
                p.getCharacter().getName(), p.getCharacter().getImageUrl(),
                p.getCharacter().getId().intValue()
            );
            dto.setLikesCount(p.getLikesCount());
            dto.setAlreadyLiked(postLikeRepository.findByAuthorCharacterAndPost(viewer, p).isPresent());
            
            // Mapeamento de comentários
            if (p.getComments() != null) {
                dto.setTopComments(p.getComments().stream()
                    .filter(c -> c.getParentComment() == null)
                    .map(this::convertCommentToDTO)
                    .collect(Collectors.toList()));
            }
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> getCharacterPosts(Integer characterId, Integer viewerId) {
        // 1. Busca os posts do personagem do perfil
        List<Post> posts = this.postRepository.findByCharacterIdOrderByDateDesc(characterId);
        GameCharacter viewer = characterRepository.findById((long) viewerId).orElse(null);

        List<PostDTO> dtos = posts.stream().map(p -> {
            // Usa o construtor completo que criamos
            PostDTO dto = new PostDTO(
                p.getId(), p.getTitle(), p.getContent(), p.getDate(), 
                p.getCharacter().getName(), p.getCharacter().getImageUrl(),
                p.getCharacter().getId().intValue()
            );
            
            dto.setLikesCount(p.getLikesCount());

            // VERIFICAÇÃO DE LIKE (Crucial para o coração aparecer vermelho)
            if (viewer != null) {
                dto.setAlreadyLiked(postLikeRepository.findByAuthorCharacterAndPost(viewer, p).isPresent());
            }

            // COMENTÁRIOS (Crucial para o número de comentários aparecer)
            if (p.getComments() != null) {
                List<CommentDTO> allComments = p.getComments().stream()
                    .filter(c -> c.getParentComment() == null)
                    .map(this::convertCommentToDTO) // Usa o método que já temos no Service
                    .collect(Collectors.toList());
                dto.setTopComments(allComments);
            }

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    // MÉTODO AUXILIAR para evitar recursão infinita e erro de Nesting Depth
    private CommentDTO convertCommentToDTO(Comment c) {
        CommentDTO cdto = new CommentDTO();
        cdto.setId(c.getId());
        cdto.setCharId(c.getCharacter().getId().intValue());
        cdto.setContent(c.getContent());
        cdto.setCharName(c.getCharacter().getName());
        cdto.setCharPhoto(c.getCharacter().getImageUrl());
        cdto.setLikesCount(c.getLikesCount());
        cdto.setDate(c.getDate());

        // Se houver respostas, converte elas também para o DTO (Hierarquia)
        if (c.getReplies() != null && !c.getReplies().isEmpty()) {
            cdto.setReplies(c.getReplies().stream()
                .map(this::convertCommentToDTO)
                .collect(Collectors.toList()));
        }
        return cdto;
    }

    public ResponseEntity<?> getSameGameFeed(int characterId) {
        GameCharacter viewer = characterRepository.findById((long) characterId)
            .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));
        
        // Busca posts do mesmo jogo do viewer
        List<Post> posts = postRepository.findByCharacterGameTitleOrderByDateDesc(viewer.getGameTitle());
        
        // Converte para DTO usando a mesma lógica que você já tem no getGlobalFeed
        List<PostDTO> dtos = posts.stream().map(p -> {
            PostDTO dto = new PostDTO(
                p.getId(), p.getTitle(), p.getContent(), p.getDate(), 
                p.getCharacter().getName(), p.getCharacter().getImageUrl(),
                p.getCharacter().getId().intValue()
            );
            dto.setLikesCount(p.getLikesCount());
            dto.setAlreadyLiked(postLikeRepository.findByAuthorCharacterAndPost(viewer, p).isPresent());
            // ... preencher comentários se necessário
            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }
}