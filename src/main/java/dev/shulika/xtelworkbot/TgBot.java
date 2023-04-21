package dev.shulika.xtelworkbot;

import dev.shulika.xtelworkbot.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class TgBot extends TelegramLongPollingBot {
    private MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            if (message.hasText()) {
                messageService.test2(message);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "Xtel_Work_Bot";
    }

    @Override
    public String getBotToken() {
        return "6269024384:AAHL-tL2fumTLhkg2Zy8l-atecL2QDxPdtc";
    }

}
