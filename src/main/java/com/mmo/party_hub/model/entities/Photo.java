package com.mmo.party_hub.model.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
@Entity
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Lob
    private byte[] content;

    @Column(length=4)
    private String extension;
    private int length;

    @OneToOne
    private Gamer g;

    @OneToOne  // Relacionamento unidirecional, a Photo "sabe" qual é o GameCharacter, mas o GameCharacter não tem referência direta à Photo
    private GameCharacter character;

    // chave primaria do banco
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Gamer getGamer() {
        return g;
    }

    public void setGamer(Gamer g) {
        this.g = g;
    }
    // adiciona getter e setter para character:
    public GameCharacter getCharacter() {
        return character;
    }
    public void setCharacter(GameCharacter character) {
        this.character = character;
    }

}
