package com.mmo.party_hub.dto;

import lombok.*;
import java.util.List;
import java.util.ArrayList;

@Getter @Setter
public class PostDTO {

    private int id;
    private String title;
    private String content;
    private long date;
    private int likesCount;
    private int characterId;
    private String charName;
    private String charPhoto;
    private boolean alreadyLiked;
    private List<CommentDTO> topComments;

    public PostDTO() {}

    public PostDTO(int id, String title, String content, long date, String charName, String charPhoto, int characterId) {
        this.id = id;

        this.title = title;
        this.content = content;
        this.date = date;
        this.likesCount = 0;  
        this.characterId = characterId; 
        this.charName = charName;
        this.charPhoto = charPhoto; 
        this.topComments = new ArrayList<>(); // Inicializa a lista de comentários
    }

}