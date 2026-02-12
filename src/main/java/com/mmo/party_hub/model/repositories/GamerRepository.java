package com.mmo.party_hub.model.repositories;

import com.mmo.party_hub.model.entities.Gamer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface GamerRepository extends JpaRepository<Gamer, Long> {
    // Método para buscar o Gamer pelo e-mail durante o login
    Optional<Gamer> findByEmail(String email);
}
