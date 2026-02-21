package com.mmo.party_hub.dto;
  
import lombok.*;

@Getter @Setter
public class NewPostDTO {

    private String title;
    private String content;
    private Long characterId;

    public NewPostDTO() { }
}