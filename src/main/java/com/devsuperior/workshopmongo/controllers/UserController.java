package com.devsuperior.workshopmongo.controllers;

import com.devsuperior.workshopmongo.models.dto.PostDTO;
import com.devsuperior.workshopmongo.models.dto.UserDTO;
import com.devsuperior.workshopmongo.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public UserController(UserService userService, ServerCodecConfigurer serverCodecConfigurer) {
        this.userService = userService;
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @GetMapping
    public Flux<UserDTO> findAll() {
        return userService.findAll();
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<UserDTO>> findById(@PathVariable String id) {
        return userService.findById(id)
                .map(userDto -> ResponseEntity.ok().body(userDto));
    }

    @PostMapping
    public Mono<ResponseEntity<UserDTO>> insert(@RequestBody UserDTO dto, UriComponentsBuilder builder) {
        return userService.insert(dto)
                .map(newUser -> ResponseEntity
                        .created(builder.path("/users/{id}").buildAndExpand(newUser.getId())
                                .toUri()).body(newUser));
    }

    @PutMapping(value = "{id}")
    public Mono<ResponseEntity<UserDTO>> update(@PathVariable String id, @RequestBody UserDTO dto) {
        return userService.update(id, dto)
                .map(userUpdated -> ResponseEntity.ok().body(userUpdated));
    }

    @DeleteMapping(value = "{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return userService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
/*
    @GetMapping(value = "/{id}/posts")
    public ResponseEntity<List<PostDTO>> getUserPosts(@PathVariable String id) {
        List<PostDTO> list = userService.getUserPosts(id);
        return ResponseEntity.ok().body(list);
    }*/
}
