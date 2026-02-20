package com.mmo.party_hub.model.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.mmo.party_hub.model.entities.GameCharacter;
import com.mmo.party_hub.model.entities.Photo;
import com.mmo.party_hub.model.repositories.GameCharacterRepository;
import com.mmo.party_hub.model.repositories.PhotoRepository;

@RestController
@RequestMapping("/photos")
@CrossOrigin(origins = "*")
public class PhotoController {

    @Autowired
    private PhotoRepository photoRepo;
    @Autowired
    private GameCharacterRepository charRepo;

    @PostMapping("/upload/{charId}")
    public ResponseEntity<?> uploadCharPhoto(@PathVariable Long charId, @RequestParam("file") MultipartFile file) {
        try {
            GameCharacter character = charRepo.findById(charId)
                .orElseThrow(() -> new RuntimeException("Personagem não encontrado"));

            Photo photo = new Photo();
            photo.setContent(file.getBytes());
            photo.setExtension(file.getContentType().split("/")[1]);
            photo.setLength((int) file.getSize());
            photo.setCharacter(character);

            photoRepo.save(photo);
            character.setImageUrl("/photos/show/" + charId);
            charRepo.save(character);

            return ResponseEntity.ok("Foto salva!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro no upload: " + e.getMessage());
        }
    }

    @GetMapping("/show/{charId}")
    public ResponseEntity<byte[]> showPhoto(@PathVariable Long charId) {
        return photoRepo.findByCharacterId(charId) 
        .map(photo -> {
            String type = photo.getExtension().toLowerCase();
            if (type.equals("jpg")) type = "jpeg";
            
            return ResponseEntity.ok()
                .header("Content-Type", "image/" + type)
                .body(photo.getContent());
        })
        .orElse(ResponseEntity.notFound().build());
    }
}