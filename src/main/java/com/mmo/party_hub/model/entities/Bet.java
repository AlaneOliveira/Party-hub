package com.mmo.party_hub.model.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private long date;
    private double value;
    private boolean answer;

    @ManyToOne
    private Gamer g;
    @ManyToOne
    private Post post;

    public Bet(){
        this.date = System.currentTimeMillis();
    }

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
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
    public boolean isAnswer() {
        return answer;
    }
    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
    public Gamer getgamer() {
        return g;
    }
    public void setGamer(Gamer gamer) {
        this.g = gamer;
    }
    public Post getPost() {
        return post;
    }
    public void setPost(Post post) {
        this.post = post;
    }



}

