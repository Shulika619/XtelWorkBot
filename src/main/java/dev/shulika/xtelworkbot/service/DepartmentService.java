package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.Department;
import dev.shulika.xtelworkbot.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    // TODO: when add Department from TelegramBot

   public List<Department> findALL() {
        log.info("+++++ IN DepartmentService :: findALL +++++");
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(Integer id) {
        log.info("+++++ IN DepartmentService :: findById :: ID - {}", id);
        return departmentRepository.findById(id);
    }

    public Optional<Department> findByName(String name) {
        log.info("+++++ IN DepartmentService :: findByName :: Name - {}", name);
        return departmentRepository.findByName(name);
    }
}