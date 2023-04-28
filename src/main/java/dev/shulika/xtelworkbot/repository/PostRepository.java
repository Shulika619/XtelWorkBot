package dev.shulika.xtelworkbot.repository;

import dev.shulika.xtelworkbot.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}