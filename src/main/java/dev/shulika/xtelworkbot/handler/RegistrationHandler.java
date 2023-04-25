package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.RegStatus;
import dev.shulika.xtelworkbot.service.AppUserService;
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

    public void regSwitch(Message message, RegStatus regStatus) {
        switch (regStatus) {
            case START_OR_CANCEL_REG -> startOrCancel(message);
            case CANCEL -> cancelReg(message);
            case COMMON_PASS -> commonPassStep1(message);
            case CHECK_COMMON_PASS -> checkCommonPassStep2(message);
            case INPUT_FULL_NAME -> inputFullNameStep3(message);
            case CHECK_INPUT_FULL_NAME -> checkInputFullNameStep4(message);
            case SELECT_DEPARTMENT -> selectDepartmentStep5(message);
            case CHECK_SELECT_DEPARTMENT -> checkSelectDepartmentStep6(message);
            default -> log.error("----- IN RegistrationHandler :: regSwitch" + COMMAND_NOT_FOUND);
        }
    }

    private void startOrCancel(Message message) {
        log.info("+++++ IN RegistrationHandler :: startOrCancel NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text(BTN_CANCEL)
                                .callbackData(BTN_CANCEL_REG_CALLBACK)
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

    private void cancelReg(Message message) {
        messageService.sendEditMessage(message, REG_MSG_CANCEL);
        appUserService.changeState(message, RegStatus.CANCEL);
    }

    private void commonPassStep1(Message message) {
        log.info("+++++ IN RegistrationHandler :: commonPassStep1 NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(BTN_CANCEL)
                                .callbackData(BTN_CANCEL_REG_CALLBACK)
                                .build()
                ));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendEditInlineKeyboardMarkup(
                message, REG_MSG_COMMON_PASS, inlineKeyboardMarkup);
        appUserService.changeState(message, RegStatus.CHECK_COMMON_PASS);
    }

    private void checkCommonPassStep2(Message message) {
        log.info("+++++ IN RegistrationHandler :: checkCommonPassStep2 NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());

        if (message.getText().equals(COMMON_PASS)) {
            log.info("+++++ IN RegistrationHandler :: checkCommonPassStep2 - OK :: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());
            messageService.deleteMsg(message);
            appUserService.changeState(message, RegStatus.INPUT_FULL_NAME);
            inputFullNameStep3(message);
        } else {
            log.info("----- IN RegistrationHandler :: checkCommonPassStep2 - FAIL:: ChatId - {}, FirstName - {}",
                    message.getChatId(), message.getChat().getFirstName());

            messageService.deleteMsg(message);
            messageService.sendMessage(message, REG_MSG_COMMON_PASS_FAIL);
        }
    }

    private void inputFullNameStep3(Message message) {
        log.info("+++++ IN RegistrationHandler :: inputFullNameStep3 NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                Collections.singletonList(
                        InlineKeyboardButton.builder()
                                .text(BTN_CANCEL)
                                .callbackData(BTN_CANCEL_REG_CALLBACK)
                                .build()
                ));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_INPUT_FULL_NAME, inlineKeyboardMarkup);
        appUserService.changeState(message, RegStatus.CHECK_INPUT_FULL_NAME);

    }
    private void checkInputFullNameStep4(Message message) {
        log.info("+++++ IN RegistrationHandler :: checkInputFullNameStep4 NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        messageService.deleteMsg(message);
        appUserService.setFullName(message);
        appUserService.changeState(message, RegStatus.SELECT_DEPARTMENT);
        selectDepartmentStep5(message);
    }

    private void selectDepartmentStep5(Message message) {
        log.info("+++++ IN RegistrationHandler :: selectDepartmentStep5 NOW:: ChatId - {}, FirstName - {}",
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
                                .callbackData(BTN_CANCEL_REG_CALLBACK)
                                .build()
                ));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendInlineKeyboardMarkup(
                message, REG_MSG_SELECT_DEPARTMENT, inlineKeyboardMarkup);

//        appUserService.changeRegStatus(message, RegStatus.CHECK_SELECT_DEPARTMENT);
    }

    private void checkSelectDepartmentStep6(Message message) {
        log.info("+++++ IN RegistrationHandler :: checkSelectDepartmentStep6 NOW:: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());

        System.out.println("++++++++++++++++++++ checkSelectDepartmentStep4 ++++++++++++++++");
    }
}