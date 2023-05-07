package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.State;
import dev.shulika.xtelworkbot.service.AppUserService;
import dev.shulika.xtelworkbot.service.DepartmentService;
import dev.shulika.xtelworkbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import static dev.shulika.xtelworkbot.BotConst.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepartmentHandler {
    private final MessageService messageService;
    private final AppUserService appUserService;
    private final DepartmentService departmentService;

    public void addDepartmentStartStep1(Message message) {
        log.info("+++++ IN DepartmentHandler :: addDepartmentStartStep1 +++++");
        appUserService.changeState(message, State.ADD_DEPARTMENT);
        messageService.sendMessage(message, ADD_DEPARTMENT_MSG);
    }

    public void addDepartmentSaveStep2(Message message) {
        log.info("+++++ IN DepartmentHandler :: addDepartmentSaveStep2 +++++");
        if (departmentService.saveDepartment(message)){
            messageService.deleteMsg(message);
            messageService.sendMessage(message, ADD_DEPARTMENT_MSG_COMPLETE);
        } else{
            messageService.deleteMsg(message);
            messageService.sendMessage(message, ADD_DEPARTMENT_MSG_FAIL);
        }
        appUserService.changeState(message, State.NONE);
    }
}
