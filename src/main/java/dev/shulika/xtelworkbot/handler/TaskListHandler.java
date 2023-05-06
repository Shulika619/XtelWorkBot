package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.Department;
import dev.shulika.xtelworkbot.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.shulika.xtelworkbot.BotConst.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class TaskListHandler {
    private final MessageService messageService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PostService postService;

    public void selectDepartmentStep1(Message message) {
        if (!employeeService.existById(message.getChatId())) {
            log.error("---- IN TaskListHandler :: selectDepartmentStep1 :: User not registered :: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());
            messageService.sendMessage(message, PROFILE_NOT_FOUND);
            return;
        }
        log.info("+++++ IN TaskListHandler :: selectDepartmentStep1 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        var departments = departmentService.findAll();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Department department : departments) {
            keyboard.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(department.getName())
                            .callbackData(BTN_DEPARTMENT_TASK_LIST_CALLBACK + ":" + department.getId())
                            .build()
            ));
        }
        keyboard.add(Collections.singletonList(messageService.createCancelButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_SELECT_DEPARTMENT, inlineKeyboardMarkup);
    }

    public void checkSelectDepartmentStep2(Message message, String value) {
        log.info("+++++ IN TaskListHandler :: checkSelectDepartmentStep2 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        var departmentId = Long.valueOf(value);
        postService.taskListDepartment(message, departmentId, -1);
    }

    public void showYesterdayTasks(Message message, String value) {
        log.info("+++++ IN TaskListHandler :: showYesterdayTasks NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        var departmentId = Long.valueOf(value);
        postService.taskListDepartment(message, departmentId, 0);
    }

    public void show2DaysTasks(Message message, String value) {
        log.info("+++++ IN TaskListHandler :: show2DaysTasks NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        var departmentId = Long.valueOf(value);
        postService.taskListDepartment(message, departmentId, 1);
    }

    public void show3DaysTasks(Message message, String value) {
        log.info("+++++ IN TaskListHandler :: show2DaysTasks NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        var departmentId = Long.valueOf(value);
        postService.taskListDepartment(message, departmentId, 2);
    }
}
