package dev.shulika.xtelworkbot.repository;

import dev.shulika.xtelworkbot.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
@Query("select p from Post p join fetch p.fromEmployee join fetch p.toDepartment join fetch p.taskExecutor " +
       "where p.toDepartment.id = :departmentId and p.createdAt >= CURRENT_DATE")
List<Post> findAllToDayPostByDepartmentId(Long departmentId);
}