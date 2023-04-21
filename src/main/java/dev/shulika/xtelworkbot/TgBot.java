package dev.shulika.xtelworkbot;

import dev.shulika.xtelworkbot.controller.UpdateController;
import dev.shulika.xtelworkbot.service.MessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.annotation.PostConstruct;

@Component
public class TgBot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    private final UpdateController updateController;

    public TgBot(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostConstruct
    public void init() {
        updateController.registerBot(this);
    }


    @Override
    public void onUpdateReceived(Update update) {
        var message = update.getMessage();
        updateController.processUpdate(update);
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
