package com.mmo.party_hub.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mmo.party_hub.model.entities.Bet;

public interface BetRepository extends JpaRepository<Bet, Integer> {

    // CORREÇÃO: O atributo agora é 'post' e não 'comment'
    @Query("select sum(b.value) from Bet b where b.post.id = :postId")
    Double sumBetValue(@Param("postId") int postId);

    // Se você usa o count, mude aqui também:
    int countByPostId(int postId);
}