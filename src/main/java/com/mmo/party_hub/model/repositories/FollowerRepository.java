package com.mmo.party_hub.model.repositories;

import com.mmo.party_hub.model.entities.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {
    // Busca quem o personagem atual segue para montar o Feed
    List<Follower> findByFollowerId(Long followerId);
    
    // Busca quem segue o personagem atual (usado no modal de 'Seguidores')
    List<Follower> findByFollowingId(Long followingId);

    // Verifica se já existe a relação de seguir para evitar duplicados
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    
    long countByFollowerId(Long followerId);
    long countByFollowingId(Long followingId);
}