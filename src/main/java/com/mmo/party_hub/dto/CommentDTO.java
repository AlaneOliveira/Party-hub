package com.mmo.party_hub.dto;
import java.util.List;

import lombok.*;

@Getter @Setter
public class CommentDTO {
    private Integer id;
    private int charId;
    private String content;
    private Integer postId;
    private Long characterId;
    private String charName;
    private String charPhoto;
    private long date;
    private int likesCount;
    private List<CommentDTO> replies; // Para os subcomentários
}