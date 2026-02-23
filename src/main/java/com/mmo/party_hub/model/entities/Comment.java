package com.mmo.party_hub.model.entities;
 
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.*;
import jakarta.persistence.CascadeType;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter @Setter
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private long date;
    
    @Lob
    private String content;

    @ManyToOne
    private Gamer gamer; // O dono da conta
    
    @ManyToOne
    private GameCharacter character; // O personagem que comentou

    @ManyToOne
    @JsonIgnore
    private Post post;

    // Lógica de Subcomentários
    @ManyToOne
    @JsonIgnore
    private Comment parentComment;

    private int likesCount = 0;
    
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> replies;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentLike> likes;

}