package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.State;
import dev.shulika.xtelworkbot.service.AppUserService;
import dev.shulika.xtelworkbot.service.EmployeeService;
import dev.shulika.xtelworkbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.shulika.xtelworkbot.BotConst.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageHandler {
    private final MessageService messageService;
    private final AppUserService appUserService;
    private final RegistrationHandler registrationHandler;
    private final EmployeeService employeeService;

    public void switchMessagesByType(Update update) {
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            messageService.sendMessage(update.getMessage(), UNSUPPORTED_MSG);
            log.info("----- IN MessageHandler :: UNSUPPORTED_MSG - {}", message);
        }
    }

    private void processTextMessage(Update update) {
        log.info("+++++ IN MessageHandler :: processTextMessage :: TEXT - {}", update.getMessage().getText());
        var message = update.getMessage();
        var user = appUserService.findUserById(message.getChatId());

        if (message.getText().equals(COMMAND_CANCEL)) {
            appUserService.cancelCommand(message);
        } else if (user == null || user.getState().equals(State.NONE) ||
                   user.getState().equals(State.CANCEL)) {
            log.info("+++++ IN MessageHandler :: STATE NULL/NONE/CANCEL +++++");
            switch (message.getText()) {
                case COMMAND_START -> appUserService.saveNewAppUser(message);
                case COMMAND_REGISTRATION -> registrationHandler.regSwitch(message, State.START_OR_CANCEL_REG);
                case COMMAND_PROFILE -> employeeService.showEmployeeInfo(message);
                case COMMAND_HELP -> messageService.sendMessage(message, HELP_MSG);
                default -> messageService.sendMessage(message, COMMAND_NOT_FOUND);
            }
        } else {
            var state = user.getState();
            log.info("+++++ IN MessageHandler :: STATE - {} NOW +++++", state);
            registrationHandler.regSwitch(message, state);
        }
    }

    private void processPhotoMessage(Update update) {
        log.info("+++++ IN MessageHandler :: processPhotoMessage :: PHOTO :: Caption - {}", update.getMessage().getCaption());
        // TODO: if change Photo+Caption -> error
//        System.out.println(update.getMessage().getCaption());
    }

    private void processDocMessage(Update update) {
        log.info("+++++ IN MessageHandler; :: processDocMessage :: DOC");
//        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
    }
}
