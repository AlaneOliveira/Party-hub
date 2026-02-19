package com.mmo.party_hub.model.repositories;
import java.util.List;
import java.util.Optional;

//import com.mmo.party_hub.model.entities.Post;
//import org.springframework.data.jpa.repository.JpaRepository;
//import java.util.List;

//public interface CommentRepository extends JpaRepository<Post, Long> {

    // verificar se o nome do método está exatamente assim, sem espaços extras
    //Optional<List<Comment>> findByAuthorEmail(String email);

   // @Query("select new com.mmo.party_hub.dto.PublicCommentDTO(p, (select sum(pl.value) from PostLike pl where pl.post.id = p.id)) from Post p")
    //public Optional<List<PublicCommentDTO>> findAvailable();
//}