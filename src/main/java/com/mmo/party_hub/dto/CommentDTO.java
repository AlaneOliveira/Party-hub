package com.mmo.party_hub.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CommentDTO {
    
    private String content;
    private int postId;

     public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }
}