package com.mmo.party_hub.dto;

public class NewPostDTO {

    private String content;
    private String description;
    private String category;

    private double betValue;
    private boolean betAnswer;

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
    public double getBetValue() {
        return betValue;
    }
    public void setBetValue(double betValue) {
        this.betValue = betValue;
    }
    public boolean isBetAnswer() {
        return betAnswer;
    }
    public void setBetAnswer(boolean betAnswer) {
        this.betAnswer = betAnswer;
    }

}

