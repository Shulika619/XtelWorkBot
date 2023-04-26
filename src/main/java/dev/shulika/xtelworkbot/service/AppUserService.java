package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.AppUser;
import dev.shulika.xtelworkbot.model.Role;
import dev.shulika.xtelworkbot.model.State;
import dev.shulika.xtelworkbot.repository.AppUserRepository;
import dev.shulika.xtelworkbot.repository.DepartmentRepository;
import dev.shulika.xtelworkbot.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.ws.rs.NotFoundException;

import static dev.shulika.xtelworkbot.BotConst.HELLO_MSG;
import static dev.shulika.xtelworkbot.BotConst.HELP_MSG;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    private final MessageService messageService;
    private final AppUserRepository appUserRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeService employeeService;

    public void saveNewAppUser(Message message) {
        var chatId = message.getChatId();
        String firstName = message.getChat().getFirstName();
        messageService.sendMessage(message, String.format("%s, %s\\! \n\n %s", HELLO_MSG, firstName, HELP_MSG));

        if (appUserRepository.findById(chatId).isEmpty()) {
            AppUser appUser = AppUser.builder()
                    .id(chatId)
                    .tgFirstName(firstName)
                    .state(State.NONE)
                    .role(Role.USER)
                    .build();
            appUserRepository.save(appUser);
            log.info("IN AppUserService :: saveNewAppUser :: ChatId - {}, FirstName - {} :: Saved", chatId, firstName);
        }
    }

    public AppUser findUserById(long chatId) {
        return appUserRepository.findById(chatId).orElse(null);
    }

    public void changeState(Message message, State state) {
        var user = appUserRepository.findById(message.getChatId())
                .orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        user.setState(state);
        log.info("+++++ IN AppUserService :: changeState :: ChatId - {}, FirstName - {}, Status - {} :: Saved",
                message.getChatId(), message.getChat().getFirstName(), state);
    }

    public void setFullName(Message message) {
        var user = appUserRepository.findById(message.getChatId())
                .orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        var newFullName = message.getText();
        user.setFullName(newFullName);
        log.info("+++++ IN AppUserService :: setFullName :: ChatId - {}, FirstName - {}, FullName - {} :: Saved",
                message.getChatId(), message.getChat().getFirstName(), newFullName);
    }

    public void setDepartmentId(Message message, long idDepartment) {
        var user = appUserRepository.findById(message.getChatId())
                .orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        user.setIdDepartment(idDepartment);
        log.info("+++++ IN AppUserService :: setDepartmentId :: ChatId - {}, FirstName - {}, DepartmentId - {} :: Saved",
                message.getChatId(), message.getChat().getFirstName(), idDepartment);
    }

    public boolean isDepartmentPassCorrect(Message message){
        var user = appUserRepository.findById(message.getChatId())
                .orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        var selectedDepartment = user.getIdDepartment();
        var department = departmentRepository.findById(selectedDepartment)
                .orElseThrow(() -> new NotFoundException("----- Department Not found-----"));
        return department.getPassword().equals(message.getText());
    }

    public void saveEmployee(Message message){
        log.info("+++++ IN AppUserService :: saveEmployee :: ChatId - {}, FirstName - {} :: START",
                message.getChatId(), message.getChat().getFirstName());
        var user = appUserRepository.findById(message.getChatId())
                .orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        employeeService.saveFromAppUser(user);
    }
}