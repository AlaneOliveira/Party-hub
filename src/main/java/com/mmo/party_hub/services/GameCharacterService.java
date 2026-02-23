package com.mmo.party_hub.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.mmo.party_hub.dto.GameCharacterDTO;
import com.mmo.party_hub.model.entities.Follower;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.model.entities.Gamer;
import com.mmo.party_hub.model.repositories.FollowerRepository;
import com.mmo.party_hub.model.repositories.GameCharacterRepository;
import com.mmo.party_hub.model.repositories.GamerRepository;
import com.mmo.party_hub.security.JwtUtils;

@Service
public class GameCharacterService {

    @Autowired
    private GameCharacterRepository characterRepository;

    @Autowired
    private GamerRepository gamerRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private FollowerRepository followerRepo;

    public GameCharacter findById(Long id) { 
        return characterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Personagem não encontrado com o ID: " + id));
    }

    public ResponseEntity<?> newGameCharacter(GameCharacterDTO dto) {
        try {
            Long idNumerico = Long.valueOf(jwtUtils.getAuthorizedId()); 
            Gamer gamer = gamerRepository.findById(idNumerico)
                .orElseThrow(() -> new RuntimeException("Gamer not found"));

            GameCharacter character = new GameCharacter();
            character.setName(dto.getName());
            character.setGameTitle(dto.getGameTitle());
            character.setImageUrl(dto.getImageUrl()); // Salva a URL enviada pelo front
            character.setGamer(gamer);
            
            GameCharacter salvo = characterRepository.save(character);
            return ResponseEntity.ok(new GameCharacterDTO(salvo)); 
            
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Erro: " + e.getMessage());
        }
    }
    
    public void updatePhotoUrl(Long id, String url) {
        GameCharacter character = findById(id);
        character.setImageUrl(url);
        characterRepository.save(character);
    }

    public ResponseEntity<?> toggleFollow(Long followerId, Long followingId) {
        if (followerId.equals(followingId)) {
            return ResponseEntity.status(400).body("Você não pode seguir a si mesmo!");
        }

        GameCharacter follower = characterRepository.findById(followerId).orElseThrow();
        GameCharacter following = characterRepository.findById(followingId).orElseThrow();

        // Busca se já segue
        var link = followerRepo.findAll().stream()
            .filter(f -> f.getFollower().getId().equals(followerId) && f.getFollowing().getId().equals(followingId))
            .findFirst();

        if (link.isPresent()) {
            followerRepo.delete(link.get());
            return ResponseEntity.ok("Deixou de seguir");
        } else {
            Follower f = new Follower();
            f.setFollower(follower);
            f.setFollowing(following);
            followerRepo.save(f);
            return ResponseEntity.ok("Seguindo");
        }
    }

    public List<GameCharacter> findByGamerId(Long gamerId) {
        return characterRepository.findByGamerId(gamerId);
    }
}