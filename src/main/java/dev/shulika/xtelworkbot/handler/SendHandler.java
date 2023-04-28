package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.Department;
import dev.shulika.xtelworkbot.model.State;
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
public class SendHandler {
    private final MessageService messageService;
    private final AppUserService appUserService;
    private final EmployeeService employeeService;
    private final DepartmentService departmentService;
    private final PostService postService;

    public void selectDepartmentStep1(Message message) {
        if (!employeeService.existById(message.getChatId())) {
            messageService.sendMessage(message, PROFILE_NOT_FOUND);
            return;
        }
        log.info("+++++ IN SendHandler :: selectDepartmentStep1 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        var departments = departmentService.findALL();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Department department : departments) {
            keyboard.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(department.getName())
                            .callbackData(BTN_DEPARTMENT_SEND_CALLBACK + ":" + department.getId())
                            .build()
            ));
        }
        keyboard.add(Collections.singletonList(messageService.createCancelButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_SELECT_DEPARTMENT, inlineKeyboardMarkup);
//        appUserService.changeState(message, State.CHECK_SELECT_DEPARTMENT);
    }

    public void checkSelectDepartmentStep2(Message message, String value) {
        log.info("+++++ IN SendHandler :: checkSelectDepartmentStep2 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(messageService.createCancelButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        appUserService.setSendTo(message, Long.valueOf(value));
        messageService.sendEditInlineKeyboardMarkup(message, SEND_MSG, inlineKeyboardMarkup);
        appUserService.changeState(message, State.SEND_MSG);
    }

    public void sendMsgTextStep3(Message message) {
        log.info("+++++ IN SendHandler :: sendMsgTextStep3 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        var postId = postService.createPost(message);
        if (postService.sendPost(postId)) {
            messageService.sendMessage(message, SEND_MSG_COMPLETE);
        } else {
            messageService.sendMessage(message, SEND_MSG_EMPTY_DEPARTMENT);
        }
        appUserService.changeState(message, State.NONE);
    }

    public void sendMsgPhotoStep3(Message message) {

        appUserService.changeState(message, State.NONE);
    }
}
