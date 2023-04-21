package dev.shulika.xtelworkbot.controller;

import dev.shulika.xtelworkbot.TgBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class UpdateController {
    private TgBot telegramBot;

    public void registerBot(TgBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void processUpdate(Update update) {
        if (update == null) {
            log.error("----- IN UpdateController :: processUpdate:: Received update is null -----");
            return;
        } else if (update.hasMessage()) {
            switchMessagesByType(update);
        } else {
            log.error("----- IN UpdateController :: processUpdate:: Unsupported message type is received: {} -----", update);
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
            setUnsupportedMessageTypeView(update);
        }
    }

    private void setFileIsReceivedView(Update update) {
//        var sendMessage = messageUtils.generateSendMessageWithText(update,
//                "Файл получен! Обрабатывается...");
//        setView(sendMessage);
    }

    public void setView(SendMessage sendMessage) {
//        telegramBot.sendAnswerMessage(sendMessage);
    }

    private void processPhotoMessage(Update update) {
//        updateProducer.produce(PHOTO_MESSAGE_UPDATE, update);
//        setFileIsReceivedView(update);
    }

    private void processDocMessage(Update update) {
//        updateProducer.produce(DOC_MESSAGE_UPDATE, update);
//        setFileIsReceivedView(update);
    }

    private void processTextMessage(Update update) {
        System.out.println("++++++ TEXT");
//        updateProducer.produce(TEXT_MESSAGE_UPDATE, update);
    }

    private void setUnsupportedMessageTypeView(Update update) {
//        var sendMessage = messageUtils.generateSendMessageWithText(update,
//                "Неподдерживаемый тип сообщения!");
//        setView(sendMessage);
    }
}
