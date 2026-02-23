package com.mmo.party_hub.model.controllers;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.mmo.party_hub.model.repositories.FollowerRepository;
import com.mmo.party_hub.dto.GameCharacterDTO;
import com.mmo.party_hub.dto.NewPasswordDTO;
import com.mmo.party_hub.model.entities.Follower;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.services.GameCharacterService;
import com.mmo.party_hub.services.GamerService;
import com.mmo.party_hub.model.repositories.GameCharacterRepository;
import com.mmo.party_hub.security.JwtUtils;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map; 

@RestController
@RequestMapping("/gamer")
public class GamerController {

    @Autowired private GamerService gamerS;
    @Autowired private GameCharacterService characterService;
    @Autowired private JwtUtils jwtUtils;
    @Autowired private FollowerRepository followerRepository;
    @Autowired private GameCharacterRepository characterRepository;

    @GetMapping("/profile")
    public ResponseEntity<?> getGamer() {
        return this.gamerS.getGamer();
    }

    @PatchMapping("/password")
    public ResponseEntity<?> newPassword (@RequestBody NewPasswordDTO newPass){
        return this.gamerS.updatePassword(newPass);
    }
 
    @PostMapping("/character")
    public ResponseEntity<?> addCaracter(@RequestBody GameCharacterDTO dto){
        return this.characterService.newGameCharacter(dto);
    }

    @GetMapping("/character/me")
    public ResponseEntity<List<GameCharacterDTO>> getMyCharacters() {

        Long gamerId = Long.valueOf(jwtUtils.getAuthorizedId());

        List<GameCharacter> list = characterService.findByGamerId(gamerId);
        
        List<GameCharacterDTO> dtos = list.stream()
            .map(GameCharacterDTO::new)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/character/{id}/photo")
    public ResponseEntity<?> uploadCharacterPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            // 1. Define a pasta onde as fotos serão salvas (ex: src/main/resources/static/uploads)
            String uploadDir = "uploads/";
            String fileName = "char_" + id + "_" + file.getOriginalFilename();
            Path path = Paths.get(uploadDir + fileName);
            
            // 2. Salva o arquivo fisicamente
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            
            // 3. Atualiza a URL no banco de dados
            String fileUrl = "/uploads/" + fileName;
            characterService.updatePhotoUrl(id, fileUrl);
            
            return ResponseEntity.ok(Map.of("url", fileUrl));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Erro ao salvar foto");
        }
    }

    @PostMapping("/character/follow")
    public ResponseEntity<?> follow(@RequestBody Map<String, Long> payload) {
        return characterService.toggleFollow(payload.get("followerId"), payload.get("followingId"));
    }

    @GetMapping("/character/{id}")
    public ResponseEntity<?> getCharacterProfile(@PathVariable Long id, @RequestParam Long viewerId) {
        GameCharacter target = characterService.findById(id);
        GameCharacterDTO dto = new GameCharacterDTO(target);
        boolean following = followerRepository.existsByFollowerIdAndFollowingId(viewerId, id);
        dto.setFollowing(following);

        return ResponseEntity.ok(dto);
    }

    @GetMapping("/character/{id}/followers")
    public ResponseEntity<List<GameCharacterDTO>> getFollowers(@PathVariable Long id) {
        List<Follower> followers = followerRepository.findByFollowingId(id);
        List<GameCharacterDTO> dtos = followers.stream()
            .map(f -> new GameCharacterDTO(f.getFollower()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/character/{id}/following")
    public ResponseEntity<List<GameCharacterDTO>> getFollowing(@PathVariable Long id) {
        List<Follower> following = followerRepository.findByFollowerId(id);
        List<GameCharacterDTO> dtos = following.stream()
            .map(f -> new GameCharacterDTO(f.getFollowing()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/character/{id}/counts")
    public ResponseEntity<?> getCounts(@PathVariable Long id) {
        long followers = followerRepository.countByFollowingId(id);
        long following = followerRepository.countByFollowerId(id);
        return ResponseEntity.ok(Map.of("followers", followers, "following", following));
    }

    @GetMapping("/character/search")
    public ResponseEntity<List<GameCharacterDTO>> searchCharacters(
            @RequestParam String name, 
            @RequestParam Long charId) {
        
        // 1. Pega o personagem que está buscando para saber o jogo (universo)
        GameCharacter viewer = characterService.findById(charId);
        
        // 2. Busca no banco por nome contendo a string E que seja do mesmo jogo
        // Use ignoreCase para facilitar a busca do usuário
        List<GameCharacter> results = characterRepository.findByNameContainingIgnoreCaseAndGameTitle(name, viewer.getGameTitle());
        
        List<GameCharacterDTO> dtos = results.stream()
            .map(GameCharacterDTO::new)
            .collect(Collectors.toList());
            
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/character/{id}/suggestions")
    public ResponseEntity<List<GameCharacterDTO>> getSuggestions(@PathVariable Long id) {
        // 1. Busca o personagem logado para saber o jogo
        GameCharacter viewer = characterRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

        // 2. Busca personagens do mesmo jogo, excluindo o próprio usuário e quem ele já segue
        // Vamos limitar a 3 ou 5 sugestões para não poluir a lateral
        List<GameCharacter> suggestions = characterRepository.findSuggestionsByGame(
            viewer.getGameTitle(), 
            id, 
            PageRequest.of(0, 3)
        );

        List<GameCharacterDTO> dtos = suggestions.stream()
            .map(GameCharacterDTO::new)
            .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    @PatchMapping("/character/{id}/photo-url")
    public ResponseEntity<?> updateUrlOnly(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String url = body.get("imageUrl");
        characterService.updatePhotoUrl(id, url); // Reutiliza o método que criamos!
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/character/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable Long id) {
        // 1. Pega o ID do Gamer logado pelo Token
        Long gamerIdFromToken = Long.valueOf(jwtUtils.getAuthorizedId());
        
        // 2. Busca o personagem
        GameCharacter character = characterService.findById(id);
        
        // 3. VALIDAÇÃO DE SEGURANÇA: O personagem pertence mesmo a quem está tentando deletar?
        if (!character.getGamer().getId().equals(gamerIdFromToken)) {
            return ResponseEntity.status(403).body("Você não tem permissão para deletar este personagem.");
        }
        
        // 4. Se for o dono, deleta
        characterRepository.delete(character);
        return ResponseEntity.ok().build();
    }

}