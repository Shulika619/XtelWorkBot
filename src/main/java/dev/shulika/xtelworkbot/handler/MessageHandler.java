package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.RegStatus;
import dev.shulika.xtelworkbot.service.MessageService;
import dev.shulika.xtelworkbot.service.RegistrationService;
import dev.shulika.xtelworkbot.service.VisitorService;
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
    private final VisitorService visitorService;
    private final RegistrationService registrationService;

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
        log.info("+++++ IN MessageHandler :: processTextMessage :: TEXT");
        var message = update.getMessage();
        switch (message.getText()) {
            case COMMAND_START -> visitorService.saveNewVisitor(message);
            case COMMAND_HELP -> messageService.sendMessage(message, HELP_MSG);
            case COMMAND_REGISTRATION -> registrationService.regSwitch(message, RegStatus.START);
            default -> messageService.sendMessage(message, COMMAND_NOT_FOUND);
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
