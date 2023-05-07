package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.Department;
import dev.shulika.xtelworkbot.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {
    @Value("${bot.common-pass}")
    private String COMMON_PASS;
    private final DepartmentRepository departmentRepository;

    public boolean saveDepartment(Message message) {
        log.info("+++++ IN DepartmentService :: saveDepartment START+++++");
        var data = message.getText();
        try {
            String[] param = data.split(":");
            String commonPass = param[0];
            String departmentName = param[1];
            String departmentPass = param[2];
            if (!commonPass.equals(COMMON_PASS) || departmentName.isBlank() || departmentPass.isBlank()){
                log.error("----- IN DepartmentService :: saveDepartment :: FAIL :: Wrong Password or null Param -----");
                return false;
            }
            var newDepartment = Department.builder()
                    .name(departmentName)
                    .password(departmentPass)
                    .build();
            departmentRepository.save(newDepartment);
            log.info("+++++ IN DepartmentService :: saveDepartment COMPLETE +++++");
            return true;
        } catch (Exception e){
            log.error("----- IN DepartmentService :: saveDepartment :: FAIL :: Error - ",e.getMessage());
            return false;
        }
    }

    public List<Department> findAll() {
        log.info("+++++ IN DepartmentService :: findALL +++++");
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(long id) {
        log.info("+++++ IN DepartmentService :: findById :: ID - {}", id);
        return departmentRepository.findById(id);
    }

    public Optional<Department> findByName(String name) {
        log.info("+++++ IN DepartmentService :: findByName :: Name - {}", name);
        return departmentRepository.findByName(name);
    }
}