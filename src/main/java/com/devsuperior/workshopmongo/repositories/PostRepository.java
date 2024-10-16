package com.devsuperior.workshopmongo.repositories;

import com.devsuperior.workshopmongo.models.entities.Post;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.Instant;
import java.util.List;

@Repository
public interface PostRepository extends ReactiveMongoRepository<Post, String> {

    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    Flux<Post> searchTitle(String text);

    @Query("{ $and: [ { $or: [ { 'title': { $regex: ?0, $options: 'i' } }, { 'body': { $regex: ?0, $options: 'i' } }, { 'comments.text': { $regex: ?0, $options: 'i' } } ] }, { 'moment': { $gte: ?1 } }, { 'moment': { $lte: ?2 } } ] }")
    Flux<Post> fullSearch(String text, Instant startMoment, Instant endMoment);

    Flux<Post> findByTitleContainingIgnoreCase(String title);

    @Query("{ 'user' : ?0 }")
    Flux<Post> findByUser(ObjectId iD);

}

