package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.AppUser;
import dev.shulika.xtelworkbot.model.Employee;
import dev.shulika.xtelworkbot.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public void saveFromAppUser(AppUser appUser){
        log.info("+++++ IN EmployeeService :: saveFromAppUser :: START +++++");
        var employee = Employee.builder()
                .id(appUser.getId())
                .idDepartment(appUser.getIdDepartment())
                .fullName(appUser.getFullName())
                .tgFirstName(appUser.getTgFirstName())
                .role(appUser.getRole())
                .build();
        employeeRepository.save(employee);
        log.info("+++++ IN EmployeeService :: saveFromAppUser :: COMPLETE +++++");
    }
}