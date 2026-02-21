package com.mmo.party_hub.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.mmo.party_hub.dto.CommentDTO;
import com.mmo.party_hub.model.entities.Comment;
import com.mmo.party_hub.model.entities.GameCharacter; 
import com.mmo.party_hub.model.entities.Post;
import com.mmo.party_hub.model.repositories.CommentRepository; 
import com.mmo.party_hub.model.repositories.PostRepository; 
import com.mmo.party_hub.model.repositories.GameCharacterRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo; 

    @Autowired
    private PostRepository postRepo; 

    @Autowired
    private GameCharacterRepository characterRepo;

    public ResponseEntity<?> createComment(CommentDTO dto) { 
        GameCharacter character = characterRepo.findById(dto.getCharacterId())
            .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        // 2. Buscamos o Post
        Post post = postRepo.findById(dto.getPostId())
            .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setDate(System.currentTimeMillis());
        
        // 3. Vinculamos o personagem e o Gamer dono dele
        comment.setCharacter(character);
        comment.setGamer(character.getGamer()); // Pega o Gamer dono do personagem automaticamente
        comment.setPost(post);

        // 4. Lógica de Subcomentário (Resposta)
        if (dto.getId() != null && dto.getId() > 0) { 
            Comment parent = commentRepo.findById(dto.getId()).orElse(null);
            comment.setParentComment(parent);
        }

        commentRepo.save(comment);
        return ResponseEntity.ok("Comentário enviado!");
    }

    /*public ResponseEntity<?> createComment(CommentDTO dto) { 
        GameCharacter character = characterRepo.findById(dto.getCharacterId())
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        Post post = postRepo.findById(dto.getPostId())
                .orElseThrow(() -> new RuntimeException("Post não encontrado"));

        Comment comment = new Comment();
        comment.setContent(dto.getContent());
        comment.setDate(System.currentTimeMillis());
        comment.setCharacter(character);
        comment.setGamer(character.getGamer());
        comment.setPost(post);

        // CORREÇÃO AQUI: Verifique se o ID não é nulo antes de comparar
        if (dto.getId() != null && dto.getId() > 0) { 
            Comment parent = commentRepo.findById(dto.getId()).orElse(null);
            comment.setParentComment(parent);
        }

        commentRepo.save(comment);
        return ResponseEntity.ok("Comentário enviado!");
    } */
}