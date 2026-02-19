package com.mmo.party_hub.model.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmo.party_hub.model.entities.Gamer;

public interface GamerRepository extends JpaRepository<Gamer, Long> {
    // Método para buscar o Gamer pelo e-mail durante o login
    Optional<Gamer> findByEmail(String email);
}
