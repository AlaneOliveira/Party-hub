package com.mmo.party_hub.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Mude para IDENTITY se estiver usando MySQL
    private int id;
    
    private long date;
    
    @Lob
    private String content;

    @ManyToOne
    private Gamer author; // No seu é Gamer, no do professor é User

    @ManyToOne
    private Post post; // Se for resposta de post, ou aponte para a Entidade principal (ex: Post/Question)
    private String description; // Adicionado para bater com seu PostDTO
    private String category;    // Adicionado para bater com seu PostDTO

    // ESTA LINHA É A QUE ESTÁ FALTANDO E CAUSA O ERRO
    @ManyToOne
    @JoinColumn(name = "character_id")
    private GameCharacter character;

    // Construtor vazio padrão
    public Post() {}

    // Getters e Setters do Character (Obrigatórios para o Hibernate)
    public GameCharacter getCharacter() {
        return character;
    }
    public void setCharacter(GameCharacter character) {
        this.character = character;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public long getDate() {
        return date;
    }
    public void setDate(long date) {
        this.date = date;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public Gamer getAuthor() {
        return author;
    }
    public void setAuthor(Gamer author) {
        this.author = author;
    }
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }
}