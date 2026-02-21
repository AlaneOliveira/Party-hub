package com.mmo.party_hub.model.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter @Setter
@Entity
@Table(name = "post_like")
public class PostLike {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private long date;
    
    @ManyToOne
    private Gamer author;
    @ManyToOne
    private Post comment; 

}
