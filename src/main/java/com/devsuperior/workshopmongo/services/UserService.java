package com.devsuperior.workshopmongo.services;

import com.devsuperior.workshopmongo.models.dto.PostDTO;
import com.devsuperior.workshopmongo.models.dto.UserDTO;
import com.devsuperior.workshopmongo.models.entities.Post;
import com.devsuperior.workshopmongo.models.entities.User;
import com.devsuperior.workshopmongo.repositories.UserRepository;
import com.devsuperior.workshopmongo.services.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Flux<UserDTO> findAll() {
        return userRepository.findAll().map(UserDTO::new);
    }

    public Mono<UserDTO> findById(String id) {
        return userRepository.findById(id)
                .map(UserDTO::new)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")));
    }

    public Mono<UserDTO> insert(UserDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        return userRepository.save(entity).map(UserDTO::new);
    }

    public Mono<UserDTO> update(String id, UserDTO dto) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setName(dto.getName());
                    existingUser.setEmail(dto.getEmail());
                    return userRepository.save(existingUser);
                })
                .map(UserDTO::new)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("User not found")));
    }
/*
    public void delete(String id) {
        getEntityById(id);
        userRepository.deleteById(id);
    }

    public List<PostDTO> getUserPosts(String id) {
        User user = getEntityById(id);
        return user.getPosts().stream().map(PostDTO::new).collect(Collectors.toList());
    }

    private User getEntityById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }
*/
    private void copyDtoToEntity(UserDTO dto, User entity) {
        entity.setName(dto.getName());
        entity.setEmail(dto.getEmail());
    }
}
