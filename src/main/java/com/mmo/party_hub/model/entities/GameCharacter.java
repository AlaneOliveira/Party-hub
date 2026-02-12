package com.mmo.party_hub.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "game_character")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class GameCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String clazz; // 'class' é reservado no Java

    private Integer level = 1;

    private String imageUrl;

    @Column(nullable = false)
    private String gameTitle;

    @ManyToOne
    @JoinColumn(name = "gamer_id", nullable = false)
    private Gamer gamer;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL)
    private List<Post> posts;
}