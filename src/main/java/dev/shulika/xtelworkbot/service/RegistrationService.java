package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.RegStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static dev.shulika.xtelworkbot.BotConst.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {
    private final MessageService messageService;
    private final AppUserService appUserService;

    public void regSwitch(Message message, RegStatus regStatus) {
        switch (regStatus) {
            case START_OR_CANCEL -> startOrCancel(message);
            case CANCEL -> cancel(message);
            case COMMON_PASS -> step1(message);
            default -> log.error("----- IN RegistrationService :: regSwitch" + COMMAND_NOT_FOUND);
        }
    }

    private void startOrCancel(Message message) {
        log.info("+++++ IN RegistrationService :: start :: ChatId - {}, FirstName - {}",
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

    private void step1(Message message) {
        log.info("+++++ IN RegistrationService :: step1 - Enter COMMON_PASS :: ChatId - {}, FirstName - {}",
                message.getChatId(), message.getChat().getFirstName());
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text(BTN_CANCEL)
                                .callbackData(BTN_CANCEL_CALLBACK)
                                .build()
                ));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        messageService.sendEditInlineKeyboardMarkup(
                message, REG_MSG_COMMON_PASS, inlineKeyboardMarkup);
        appUserService.changeRegStatus(message, RegStatus.COMMON_PASS);
    }

}