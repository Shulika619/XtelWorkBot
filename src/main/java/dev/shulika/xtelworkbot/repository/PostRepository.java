package dev.shulika.xtelworkbot.repository;

import dev.shulika.xtelworkbot.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p join fetch p.fromEmployee join fetch p.toDepartment " +
           "where p.toDepartment.id = :departmentId and p.createdAt >= CURRENT_DATE-:interval order by p.id")
    List<Post> findAllPostByDepartmentId(Long departmentId, int interval);
}