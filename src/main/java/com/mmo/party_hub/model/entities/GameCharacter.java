package com.mmo.party_hub.model.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @JsonIgnore // Evita referência circular na serialização JSON
    private Gamer gamer;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL)
    private List<Post> posts;
}