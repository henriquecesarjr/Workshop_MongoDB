package com.devsuperior.workshopmongo.services;

import com.devsuperior.workshopmongo.models.dto.PostDTO;
import com.devsuperior.workshopmongo.models.dto.UserDTO;
import com.devsuperior.workshopmongo.models.entities.Post;
import com.devsuperior.workshopmongo.models.entities.User;
import com.devsuperior.workshopmongo.repositories.PostRepository;
import com.devsuperior.workshopmongo.services.exceptions.ResourceNotFoundException;
import org.bson.types.BSONTimestamp;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.OrientationRequested;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PostDTO findById(String id) {
        Post entity = getEntityById(id);
        return new PostDTO(entity);
    }

    public List<PostDTO> findByTitle(String text) {
        List<Post> list = postRepository.searchTitle(text);
        return list.stream().map(PostDTO::new).collect(Collectors.toList());
    }

    public List<PostDTO> fullSearch(String text, String start, String end) {
        Instant startMoment = convertMoment(start, Instant.ofEpochMilli(0L));
        Instant endMoment = convertMoment(end, Instant.now());

        List<Post> list = postRepository.fullSearch(text, startMoment, endMoment);
        return list.stream().map(PostDTO::new).collect(Collectors.toList());
    }

    private Post getEntityById(String id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
    }

    private Instant convertMoment(String originalString, Instant alternative) {
        try {
            return Instant.parse(originalString);
        } catch (DateTimeParseException e) {
            return alternative;
        }
    }
}
