package com.mmo.party_hub.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "likee")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private long date;
    
    @ManyToOne
    private Gamer author;
    @ManyToOne
    private Post comment;

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
    public Gamer getAuthor() {
        return author;
    }
    public void setAuthor(Gamer author) {
        this.author = author;
    }
    public Post getComment() {
        return comment;
    }
    public void setComment(Post comment) {
        this.comment = comment;
    }

}
