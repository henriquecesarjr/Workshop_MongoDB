package com.devsuperior.workshopmongo.repositories;

import com.devsuperior.workshopmongo.models.entities.Post;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {

    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Post> searchTitle(String text);

    @Query("{ $and: [ { $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'body': { $regex: ?0, $options: 'i' } }, { 'comments.text': { $regex: ?0, $options: 'i' } } ] }, { 'moment': { $gte: ?1 } }, { 'moment': { $lte: ?2 } } ] }")
    List<Post> fullSearch(String text, Instant startMoment, Instant endMoment);

    List<Post> findByTitleContainingIgnoreCase(String title);

}

