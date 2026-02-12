package com.mmo.party_hub.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "follower")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Follower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private GameCharacter follower;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private GameCharacter following;
}