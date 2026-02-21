package com.mmo.party_hub.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.*;
import java.util.List; 

@Getter @Setter
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private long date;
    
    @Lob
    private String content; 
    private String title;    

    @ManyToOne
    private Gamer author; 
  
    @ManyToOne
    @JoinColumn(name = "character_id")
    private GameCharacter character;

    @OneToMany(mappedBy = "post") // O campo 'post' na classe Comment manda aqui
    private List<Comment> comments;

    private int likesCount = 0; 

    // Construtor vazio padrão
    public Post() {} 
} 