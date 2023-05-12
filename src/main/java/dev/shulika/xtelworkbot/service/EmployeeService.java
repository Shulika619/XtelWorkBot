package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.BotConst;
import dev.shulika.xtelworkbot.model.AppUser;
import dev.shulika.xtelworkbot.model.Department;
import dev.shulika.xtelworkbot.model.Employee;
import dev.shulika.xtelworkbot.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.text.SimpleDateFormat;

import static dev.shulika.xtelworkbot.BotConst.PROFILE_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final MessageService messageService;

    public void saveFromAppUser(AppUser appUser) {
        log.info("+++++ IN EmployeeService :: saveFromAppUser :: ChatId = {}, FullName - {} :: START",
                appUser.getId(), appUser.getFullName());
        var checkEmployee = employeeRepository.findById(appUser.getId());
        if (checkEmployee.isPresent()) {
            var employee = checkEmployee.get();
            editEmployee(employee, appUser);
        } else {
            var newEmployee = Employee.builder()
                    .id(appUser.getId())
                    .department(Department.builder()
                            .id(appUser.getDepartmentId())
                            .build())
                    .fullName(appUser.getFullName())
                    .tgFirstName(appUser.getTgFirstName())
                    .role(appUser.getRole())
                    .build();
            employeeRepository.save(newEmployee);
            log.info("+++++ IN EmployeeService :: saveFromAppUser :: ChatId = {}, FullName - {} :: COMPLETE",
                    appUser.getId(), appUser.getFullName());
        }
    }

    private void editEmployee(Employee employee, AppUser appUser) {
        log.info("+++++ IN EmployeeService :: editEmployee :: ChatId = {}, FullName - {} :: START",
                appUser.getId(), appUser.getFullName());
        employee.setDepartment(Department.builder()
                .id(appUser.getDepartmentId()).build());
        employee.setFullName(appUser.getFullName());
        employee.setTgFirstName(appUser.getTgFirstName());
        employee.setRole(appUser.getRole());
        log.info("+++++ IN EmployeeService :: editEmployee :: ChatId = {}, FullName - {} :: COMPLETE",
                appUser.getId(), appUser.getFullName());
    }

    public boolean existById(Long employeeId) {
        return employeeRepository.existsById(employeeId);
    }

    public void showEmployeeInfo(Message message) {
        log.info("+++++ IN EmployeeService :: showEmployeeInfo :: ChatId - {} +++++", message.getChatId());
        var checkEmployee = employeeRepository.findById(message.getChatId());
        if (checkEmployee.isPresent()) {
            var employee = checkEmployee.get();
            var sendMsg = new StringBuilder();
            sendMsg.append(BotConst.PROFILE_MSG);
            sendMsg.append(String.format("_ID:_ `%s`\n", employee.getId()));
            sendMsg.append(String.format("_Фамилия Имя:_ `%s`\n", employee.getFullName()));
            sendMsg.append(String.format("_Telegram Имя:_ `%s`\n", employee.getTgFirstName()));
            sendMsg.append(String.format("_Отдел/магазин:_ `%s`\n", employee.getDepartment().getName()));
            sendMsg.append(String.format("_Права доступа:_ `%s`\n", employee.getRole()));
            sendMsg.append(String.format("_Last update:_ `%s`\n",
                    new SimpleDateFormat("dd/MM/yyyy").format(employee.getUpdatedAt())));
            messageService.sendMessage(message, sendMsg.toString());
        } else {
            messageService.sendMessage(message, PROFILE_NOT_FOUND);
        }
    }
}