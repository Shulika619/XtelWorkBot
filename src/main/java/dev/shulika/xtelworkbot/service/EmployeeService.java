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
        log.info("+++++ IN EmployeeService :: saveFromAppUser :: ChatId = {}, FullName - {} :: START",
                appUser.getId(), appUser.getFullName());
        var checkEmployee = employeeRepository.findById(appUser.getId());
        if (checkEmployee.isPresent()){
            var employee = checkEmployee.get();
            editEmployee(employee, appUser);
        } else {
            var newEmployee = Employee.builder()
                    .id(appUser.getId())
                    .idDepartment(appUser.getIdDepartment())
                    .fullName(appUser.getFullName())
                    .tgFirstName(appUser.getTgFirstName())
                    .role(appUser.getRole())
                    .build();
            employeeRepository.save(newEmployee);
            log.info("+++++ IN EmployeeService :: saveFromAppUser :: ChatId = {}, FullName - {} :: COMPLETE",
                    appUser.getId(), appUser.getFullName());
        }
    }

    private void editEmployee(Employee employee, AppUser appUser){
        log.info("+++++ IN EmployeeService :: editEmployee :: ChatId = {}, FullName - {} :: START",
                appUser.getId(), appUser.getFullName());
        employee.setIdDepartment(appUser.getIdDepartment());
        employee.setFullName(appUser.getFullName());
        employee.setTgFirstName(appUser.getTgFirstName());
        employee.setRole(appUser.getRole());
        log.info("+++++ IN EmployeeService :: editEmployee :: ChatId = {}, FullName - {} :: COMPLETE",
                appUser.getId(), appUser.getFullName());
    }
}