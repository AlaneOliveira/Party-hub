package com.mmo.party_hub.model.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mmo.party_hub.model.entities.Photo;

// aqui criamos uma interface para que seja usada por outras classes e não precise fazer código direto
public interface PhotoRepository extends JpaRepository<Photo,Integer>{ //é uma interface do Spring Data JPA que você usa para acessar o banco de dados sem precisar escrever SQL manualmente. Já vem com métodos prontos: deletar, salvar, buscar, atualizar e paginação
    
    Optional<Photo> findByGamerId(Long gamerId);

}
