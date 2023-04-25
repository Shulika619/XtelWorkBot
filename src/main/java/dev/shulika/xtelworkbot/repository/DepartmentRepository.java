package dev.shulika.xtelworkbot.repository;

import dev.shulika.xtelworkbot.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
}