package com.mmo.party_hub.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_like")
@Getter @Setter

public class PostLike {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "character_id")
    private GameCharacter character;
}