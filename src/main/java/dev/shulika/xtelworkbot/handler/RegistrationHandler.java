package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.Department;
import dev.shulika.xtelworkbot.model.State;
import dev.shulika.xtelworkbot.service.AppUserService;
import dev.shulika.xtelworkbot.service.DepartmentService;
import dev.shulika.xtelworkbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.shulika.xtelworkbot.BotConst.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RegistrationHandler {
    @Value("${bot.common-pass}")
    private String COMMON_PASS;
    private final MessageService messageService;
    private final AppUserService appUserService;
    private final DepartmentService departmentService;

    public void regSwitch(Message message, State state) {
        switch (state) {
            case START_OR_CANCEL_REG -> startOrCancel(message);
            case COMMON_PASS -> commonPassStep1(message);
            case CHECK_COMMON_PASS -> checkCommonPassStep2(message);
            case INPUT_FULL_NAME -> inputFullNameStep3(message);
            case CHECK_INPUT_FULL_NAME -> checkInputFullNameStep4(message);
            case SELECT_DEPARTMENT -> selectDepartmentStep5(message);
//            case CHECK_SELECT_DEPARTMENT -> checkSelectDepartmentStep6(message);
            case CHECK_DEPARTMENT_PASS -> checkDepartmentPass7(message);
            default -> log.error("----- IN RegistrationHandler :: regSwitch - " + COMMAND_NOT_FOUND);
        }
    }

    private void startOrCancel(Message message) {
        log.info("+++++ IN RegistrationHandler :: startOrCancel NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(
                messageService.createCancelButton(),
                InlineKeyboardButton.builder()
                        .text(BTN_START_REG)
                        .callbackData(BTN_START_REG_CALLBACK +":" + null)
                        .build()
        ));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_TITLE, inlineKeyboardMarkup);
    }

    private void commonPassStep1(Message message) {
        log.info("+++++ IN RegistrationHandler :: commonPassStep1 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(messageService.createCancelButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendEditInlineKeyboardMarkup(
                message, REG_MSG_COMMON_PASS, inlineKeyboardMarkup);
        appUserService.changeState(message, State.CHECK_COMMON_PASS);
    }

    private void checkCommonPassStep2(Message message) {
        log.info("+++++ IN RegistrationHandler :: checkCommonPassStep2 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        if (message.getText().equals(COMMON_PASS)) {
            log.info("+++++ IN RegistrationHandler :: checkCommonPassStep2 - OK :: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());
            messageService.deleteMsg(message);
            appUserService.changeState(message, State.INPUT_FULL_NAME);
            inputFullNameStep3(message);
        } else {
            log.error("----- IN RegistrationHandler :: checkCommonPassStep2 - FAIL :: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());
            messageService.deleteMsg(message);
            messageService.sendMessage(message, REG_MSG_COMMON_PASS_FAIL);
        }
    }

    private void inputFullNameStep3(Message message) {
        log.info("+++++ IN RegistrationHandler :: inputFullNameStep3 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(messageService.createCancelButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_INPUT_FULL_NAME, inlineKeyboardMarkup);
        appUserService.changeState(message, State.CHECK_INPUT_FULL_NAME);
    }

    private void checkInputFullNameStep4(Message message) {
        log.info("+++++ IN RegistrationHandler :: checkInputFullNameStep4 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        messageService.deleteMsg(message);
        appUserService.setFullName(message);
        appUserService.changeState(message, State.SELECT_DEPARTMENT);
        selectDepartmentStep5(message);
    }

    private void selectDepartmentStep5(Message message) {
        log.info("+++++ IN RegistrationHandler :: selectDepartmentStep5 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        var departments = departmentService.findALL();
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        for (Department department : departments) {
            keyboard.add(List.of(
                    InlineKeyboardButton.builder()
                            .text(department.getName())
                            .callbackData(BTN_DEPARTMENT_REG_CALLBACK + ":" + department.getId())
                            .build()
            ));
        }
        keyboard.add(Collections.singletonList(messageService.createCancelButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_SELECT_DEPARTMENT, inlineKeyboardMarkup);
        appUserService.changeState(message, State.CHECK_SELECT_DEPARTMENT);
    }

    public void checkSelectDepartmentStep6(Message message, String value) {
        log.info("+++++ IN RegistrationHandler :: checkSelectDepartmentStep6 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(messageService.createCancelButton()));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        appUserService.setDepartmentId(message, Integer.parseInt(value));
        appUserService.changeState(message, State.CHECK_DEPARTMENT_PASS);
        messageService.sendEditInlineKeyboardMarkup(message, REG_MSG_DEPARTMENT_PASS, inlineKeyboardMarkup);
    }

    private void checkDepartmentPass7(Message message) {
        log.info("+++++ IN RegistrationHandler :: checkDepartmentPass7 NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        if (appUserService.isDepartmentPassCorrect(message)) {
            log.info("+++++ IN RegistrationHandler :: checkDepartmentPass7 - OK :: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());
            messageService.deleteMsg(message);
            finishRegistration(message);
        } else {
            log.error("----- IN RegistrationHandler :: checkDepartmentPass7 - FAIL :: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());
            messageService.deleteMsg(message);
            messageService.sendMessage(message, REG_MSG_DEPARTMENT_PASS_FAIL);
        }
    }

    // TODO: select ROLE

    private void finishRegistration(Message message) {
        log.info("+++++ IN RegistrationHandler :: finishRegistration NOW :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        appUserService.saveEmployee(message);
        messageService.sendMessage(message, REG_MSG_REG_COMPLETE);
        appUserService.changeState(message, State.NONE);
    }
}