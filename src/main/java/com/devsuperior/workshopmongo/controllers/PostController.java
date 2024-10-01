package com.devsuperior.workshopmongo.controllers;

import com.devsuperior.workshopmongo.models.dto.PostDTO;
import com.devsuperior.workshopmongo.models.dto.UserDTO;
import com.devsuperior.workshopmongo.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PostDTO> findById(@PathVariable String id) {
        PostDTO obj = postService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping(value = "/titlesearch")
    public ResponseEntity<List<PostDTO>> findByTitle(@RequestParam(value = "text", defaultValue = "") String text) {
        List<PostDTO> list = postService.findByTitle(text);
        return ResponseEntity.ok().body(list);
    }

}
