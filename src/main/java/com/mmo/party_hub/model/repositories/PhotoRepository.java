package com.mmo.party_hub.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmo.party_hub.model.entities.Photo;

public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    Optional<Photo> findByGId(Long gId); // procura o registro de foto associada a um gamer específico, usando o ID do gamer como critério de busca. Retorna um Optional que pode conter a foto encontrada ou estar vazio se não houver foto para aquele gamer.
    Optional<Photo> findByCharacterId(Long characterId); // procura o registro de foto associada a um personagem específico, usando o ID do personagem como critério de busca. Retorna um Optional que pode conter a foto encontrada ou estar vazio se não houver foto para aquele personagem.
}

