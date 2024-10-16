package com.devsuperior.workshopmongo.config;

import com.devsuperior.workshopmongo.models.embedded.Author;
import com.devsuperior.workshopmongo.models.embedded.Comment;
import com.devsuperior.workshopmongo.models.entities.Post;
import com.devsuperior.workshopmongo.models.entities.User;
import com.devsuperior.workshopmongo.repositories.PostRepository;
import com.devsuperior.workshopmongo.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

@Configuration
@Profile("test")
public class TestConfig {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public TestConfig(UserRepository userRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @PostConstruct
    public void init() throws ExecutionException, InterruptedException {

        Mono<Void> deleteUsers = userRepository.deleteAll();
        deleteUsers.subscribe();

        Mono<Void> deletePosts = postRepository.deleteAll();
        deletePosts.subscribe();

        User maria = new User(null, "Maria Brown", "maria@gmail.com");
        User alex = new User(null, "Alex Green", "alex@gmail.com");
        User bob = new User(null, "Bob Grey", "bob@gmail.com");

        Flux<User> insertUsers = userRepository.saveAll(Arrays.asList(maria, alex, bob));
        insertUsers.subscribe();

        maria = userRepository.searchEmail("maria@gmail.com").toFuture().get();
        alex = userRepository.searchEmail("alex@gmail.com").toFuture().get();
        bob = userRepository.searchEmail("bob@gmail.com").toFuture().get();

        Post post1 = new Post(null, Instant.parse("2021-02-13T11:15:01Z"), "Partiu viagem", "Vou viajar para São Paulo. Abraços!", new Author(maria));
        Post post2 = new Post(null, Instant.parse("2021-02-14T10:05:49Z"), "Bom dia", "Acordei feliz hoje!", new Author(maria));

        Comment c1 = new Comment("Boa viagem mano!", Instant.parse("2021-02-13T14:30:01Z"), new Author(alex));
        Comment c2 = new Comment("Aproveite", Instant.parse("2021-02-13T15:38:05Z"), new Author(bob));
        Comment c3 = new Comment("Tenha um ótimo dia!", Instant.parse("2021-02-14T12:34:26Z"), new Author(alex));

        post1.getComments().addAll(Arrays.asList(c1, c2));
        post2.getComments().addAll(Arrays.asList(c3));

        post1.setUser(userRepository.searchEmail("maria@gmail.com").block());
        post2.setUser(userRepository.searchEmail("maria@gmail.com").block());

        Flux<Post> insertPosts = postRepository.saveAll(Arrays.asList(post1, post2));
        insertPosts.subscribe();

    }

}
