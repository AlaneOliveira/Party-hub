package com.mmo.party_hub.model.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "gamer")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Gamer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @OneToMany(mappedBy = "gamer", cascade = CascadeType.ALL)
    private List<GameCharacter> characters;
}