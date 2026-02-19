package com.mmo.party_hub.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private long date;
    
    @Lob
    private String content;

    @ManyToOne
    private Gamer gamer;
    
    @ManyToOne
    private Post post;

    // Getters e Setters limpos
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public long getDate() { return date; }
    public void setDate(long date) { this.date = date; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Gamer getGamer() { return gamer; }
    public void setGamer(Gamer gamer) { this.gamer = gamer; }
    
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
}