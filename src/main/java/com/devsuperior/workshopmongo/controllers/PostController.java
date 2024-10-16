package com.devsuperior.workshopmongo.controllers;

import com.devsuperior.workshopmongo.controllers.util.URL;
import com.devsuperior.workshopmongo.models.dto.PostDTO;
import com.devsuperior.workshopmongo.models.dto.UserDTO;
import com.devsuperior.workshopmongo.services.PostService;
import com.devsuperior.workshopmongo.services.UserService;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/{id}")
    public Mono<ResponseEntity<PostDTO>> findById(@PathVariable String id) {
        return postService.findById(id)
                .map(postDto -> ResponseEntity.ok().body(postDto));
    }

    @GetMapping(value = "/titlesearch")
    public Flux<PostDTO> findByTitle(@RequestParam(value = "text", defaultValue = "") String text) throws UnsupportedEncodingException {
        text = URL.decodeParam(text);
        return postService.findByTitle(text);
    }

    @GetMapping(value = "/fullsearch")
    public Flux<PostDTO> fullSearch (
            @RequestParam(value = "text", defaultValue = "") String text,
            @RequestParam(value = "minDate", defaultValue = "") String minDate,
            @RequestParam(value = "maxDate", defaultValue = "") String maxDate) throws UnsupportedEncodingException
    {
        text = URL.decodeParam(text);
        Instant min;
        Instant max;
        try {
            min = URL.convertDate(minDate, Instant.EPOCH);
            max = URL.convertDate(maxDate, Instant.now());
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format", e); // Ou uma resposta mais apropriada
        }

        return postService.fullSearch(text, min, max);
    }

}
