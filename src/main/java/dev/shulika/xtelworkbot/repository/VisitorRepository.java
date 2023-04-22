package dev.shulika.xtelworkbot.repository;

import dev.shulika.xtelworkbot.model.Visitor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitorRepository extends JpaRepository<Visitor, Long> {
}