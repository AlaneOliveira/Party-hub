package com.mmo.party_hub.dto;

public class PostLikeDTO {
    private int postId;
    private int characterId; 

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getCharacterId() { return characterId; }
    public void setCharacterId(int characterId) { this.characterId = characterId; }
}