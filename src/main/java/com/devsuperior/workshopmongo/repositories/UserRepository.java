package com.devsuperior.workshopmongo.repositories;

import com.devsuperior.workshopmongo.models.entities.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {
}
