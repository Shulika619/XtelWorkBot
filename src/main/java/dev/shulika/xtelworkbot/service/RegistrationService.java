package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.RegStatus;
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
public class RegistrationService {
    @Value("${bot.common-pass}")
    private String COMMON_PASS;
    private final MessageService messageService;
    private final AppUserService appUserService;

    public void regSwitch(Message message, RegStatus regStatus) {
        switch (regStatus) {
            case START_OR_CANCEL -> startOrCancel(message);
            case CANCEL -> cancel(message);
            case COMMON_PASS -> commonPassStep1(message);
            case CHECK_COMMON_PASS -> checkCommonPassStep2(message);
            case SELECT_DEPARTMENT -> selectDepartmentStep3(message);
            case CHECK_SELECT_DEPARTMENT -> checkSelectDepartmentStep4(message);
            default -> log.error("----- IN RegistrationService :: regSwitch" + COMMAND_NOT_FOUND);
        }
    }

    private void startOrCancel(Message message) {
        log.info("+++++ IN RegistrationService :: startOrCancel NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text(BTN_CANCEL)
                                .callbackData(BTN_CANCEL_CALLBACK)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text(BTN_START_REG)
                                .callbackData(BTN_START_REG_CALLBACK)
                                .build()
                ));
        inlineKeyboardMarkup.setKeyboard(keyboard);

        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_TITLE, inlineKeyboardMarkup);
    }

    private void cancel(Message message) {
        messageService.sendEditMessage(message, REG_MSG_CANCEL);
        appUserService.changeRegStatus(message, RegStatus.CANCEL);
    }

    private void commonPassStep1(Message message) {
        log.info("+++++ IN RegistrationService :: commonPassStep1 NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(BTN_CANCEL)
                                .callbackData(BTN_CANCEL_CALLBACK)
                                .build()
                ));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendEditInlineKeyboardMarkup(
                message, REG_MSG_COMMON_PASS, inlineKeyboardMarkup);
        appUserService.changeRegStatus(message, RegStatus.CHECK_COMMON_PASS);
    }

    private void checkCommonPassStep2(Message message) {
        log.info("+++++ IN RegistrationService :: checkCommonPassStep2 NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());

        if (message.getText().equals(COMMON_PASS)) {
            log.info("+++++ IN RegistrationService :: checkCommonPassStep2 - OK :: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());
            messageService.deleteMsg(message);
            appUserService.changeRegStatus(message, RegStatus.SELECT_DEPARTMENT);
            selectDepartmentStep3(message);
        } else {
            log.info("----- IN RegistrationService :: checkCommonPassStep2 - FAIL:: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());

            messageService.deleteMsg(message);
            messageService.sendMessage(message, REG_MSG_COMMON_PASS_FAIL);
        }
    }

    private void selectDepartmentStep3(Message message) {
        log.info("+++++ IN RegistrationService :: selectDepartmentStep3 NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Маг1")
                                .callbackData("mag1")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Маг2")
                                .callbackData("mag2")
                                .build()
                ));
        keyboard.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(BTN_CANCEL)
                                .callbackData(BTN_CANCEL_CALLBACK)
                                .build()
                ));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_SELECT_DEPARTMENT, inlineKeyboardMarkup);

//        appUserService.changeRegStatus(message, RegStatus.CHECK_SELECT_DEPARTMENT);
    }

    private void checkSelectDepartmentStep4(Message message) {
        System.out.println("++++++++++++++++++++ checkSelectDepartmentStep4 ++++++++++++++++");
    }
}