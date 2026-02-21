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
    private int likes;
    private String charName;
    private String charPhoto;
    private List<CommentDTO> topComments;

    public PostDTO() {}

    public PostDTO(int id, String title, String content, long date, String charName, String charPhoto) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.likes = 0; // Inicializa com 0, será atualizado depois
        this.charName = charName;
        this.charPhoto = charPhoto; 
        this.topComments = new ArrayList<>(); // Inicializa a lista de comentários
    }

}