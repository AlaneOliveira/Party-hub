package com.mmo.party_hub.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.mmo.party_hub.model.entities.GameCharacter;

@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GamerCharacterDTO {

    private Long id;
    private String name;
    private String clazz;
    private String gameTitle;
    private Integer level;
    private String imageUrl;
    private boolean hasPhoto;

    // Construtor para converter Entidade em DTO
    public GamerCharacterDTO(GameCharacter character) {
        this.id = character.getId();
        this.name = character.getName();
        this.clazz = character.getClazz();
        this.gameTitle = character.getGameTitle();
        this.level = character.getLevel();
        this.imageUrl = character.getImageUrl();
        this.hasPhoto = character.getImageUrl() != null && !character.getImageUrl().isEmpty();
    }
}