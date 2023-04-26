package dev.shulika.xtelworkbot.repository;

import dev.shulika.xtelworkbot.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}