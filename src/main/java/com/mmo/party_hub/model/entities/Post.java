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
    private String content; // Este será o corpo/descrição do post
    private String title;   // Novo campo para o título

    @ManyToOne
    private Gamer author; // No seu é Gamer, no do professor é User
 
    // ESTA LINHA É A QUE ESTÁ FALTANDO E CAUSA O ERRO
    @ManyToOne
    @JoinColumn(name = "character_id")
    private GameCharacter character;

    @OneToMany(mappedBy = "post") // O campo 'post' na classe Comment manda aqui
    private List<Comment> comments;

    // Construtor vazio padrão
    public Post() {} 
} 