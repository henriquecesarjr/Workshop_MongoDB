package com.devsuperior.workshopmongo.repositories;

import com.devsuperior.workshopmongo.models.entities.Post;
import com.devsuperior.workshopmongo.models.entities.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {

    List<Post> findByTitleContainingIgnoreCase(String title);

}
