package dev.shulika.xtelworkbot.controller;

import dev.shulika.xtelworkbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.shulika.xtelworkbot.BotConst.PROCESSED;
import static dev.shulika.xtelworkbot.BotConst.UNSUPPORTED_MSG;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpdateController {
    private final MessageService messageService;

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("----- IN UpdateController :: processUpdate:: Received update is null -----");
            return;
        } else if (update.hasMessage()) {
            switchMessagesByType(update);
        } else {
            log.error("----- IN UpdateController :: processUpdate:: Unsupported message type is received - {} -----", update);
        }
    }

    private void switchMessagesByType(Update update) {
        var message = update.getMessage();
        if (message.hasText()) {
            processTextMessage(update);
        } else if (message.hasDocument()) {
            processDocMessage(update);
        } else if (message.hasPhoto()) {
            processPhotoMessage(update);
        } else {
            messageService.sendResponseWithMarkDownV2(update.getMessage(), UNSUPPORTED_MSG);
            log.info("----- IN UpdateController :: UNSUPPORTED_MSG - {}", message);
        }
    }

    private void processed(Update update) {
        log.info("..... IN UpdateController :: processed Message .....");
        messageService.sendResponseWithMarkDownV2(update.getMessage(), PROCESSED);
    }

    private void processTextMessage(Update update) {
        log.info("++++++ IN UpdateController :: processTextMessage :: TEXT");
        messageService.sendResponseWithMarkDownV2(update.getMessage(), PROCESSED);
//        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void processPhotoMessage(Update update) {
        log.info("++++++ IN UpdateController :: processPhotoMessage :: PHOTO :: Caption - {}", update.getMessage().getCaption());
        // TODO: if change Photo+Caption -> error
//        System.out.println(update.getMessage().getCaption());
//        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
//        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
        log.info("++++++ IN UpdateController :: processDocMessage :: DOC");
//        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
//        setFileIsReceivedView(update);
    }
}
