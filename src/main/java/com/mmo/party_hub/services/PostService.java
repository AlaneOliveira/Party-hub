package com.mmo.party_hub.services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Comparator; 
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
        List<Post> posts = this.postRepository.findAllByOrderByDateDesc();
        GameCharacter viewer = characterRepository.findById((long) characterId).orElse(null);
        List<PostDTO> dtos = posts.stream().map(p -> {
            PostDTO dto = new PostDTO(
                p.getId(), 
                p.getTitle(), 
                p.getContent(), 
                p.getDate(), 
                p.getCharacter().getName(), 
                p.getCharacter().getImageUrl(),
                p.getCharacter().getId().intValue()
            );
            
            // Contagem de likes do post
            dto.setLikesCount(p.getLikesCount());

            if (viewer != null) { 
                boolean liked = postLikeRepository.findByAuthorCharacterAndPost(viewer, p).isPresent();
                dto.setAlreadyLiked(liked);
            }

            // Lógica de Comentários Populares
            if (p.getComments() != null) {
                List<CommentDTO> allComments = p.getComments().stream()
                    .filter(c -> c.getParentComment() == null) // Apenas os comentários "pai"
                    .sorted(Comparator.comparingLong(Comment::getDate)) // Ordem de chegada (antigos primeiro)
                    .map(this::convertCommentToDTO)
                    .collect(Collectors.toList());
                
                dto.setTopComments(allComments); // O nome do campo pode continuar o mesmo no DTO, mas agora leva todos
            }

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    public ResponseEntity<?> getCharacterPosts(Integer characterId) {
        List<Post> posts = this.postRepository.findByCharacterIdOrderByDateDesc(characterId);
        List<PostDTO> dtos = posts.stream().map(p -> new PostDTO(
            p.getId(), 
            p.getTitle(), 
            p.getContent(), 
            p.getDate(), 
            p.getCharacter().getName(), 
            p.getCharacter().getImageUrl(),
            p.getCharacter().getId().intValue()
        )).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // MÉTODO AUXILIAR para evitar recursão infinita e erro de Nesting Depth
    private CommentDTO convertCommentToDTO(Comment c) {
        CommentDTO cdto = new CommentDTO();
        cdto.setId(c.getId());
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
}