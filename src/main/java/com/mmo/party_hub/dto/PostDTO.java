package com.mmo.party_hub.dto;

public class PostDTO {

    private int id;
    private String content;
    private String description;
    private String category;
    private long date;
    private int bets;
    private double pot;
    private int likes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getBets() {
        return bets;
    }

    public void setBets(int bets) {
        this.bets = bets;
    }

    public double getPot() {
        return pot;
    }

    public void setPot(double pot) {
        this.pot = pot;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    // Construtor vazio essencial para o Jackson/Spring
    public PostDTO() {
    }

    // Seu construtor customizado (igual ao do professor)
    public PostDTO(int id, String content, String description, String category, long created) {
        this.id = id;
        this.content = content;
        this.description = description;
        this.category = category;
        this.date = created;
    }

}


