package dev.shulika.xtelworkbot.controller;

import dev.shulika.xtelworkbot.handler.CallbackQueryHandler;
import dev.shulika.xtelworkbot.handler.MessageHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Controller
@Slf4j
public class TgBot extends TelegramLongPollingBot {
    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;
    private MessageHandler messageHandler;
    private CallbackQueryHandler callbackQueryHandler;

    @Autowired
    public void TgBot(MessageHandler messageHandler, CallbackQueryHandler callbackQueryHandler) {
        this.messageHandler = messageHandler;
        this.callbackQueryHandler = callbackQueryHandler;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            messageHandler.switchMessagesByType(update);
        } else if (update.hasCallbackQuery()) {
            callbackQueryHandler.switchCallbackQueryByType(update);
        } else {
            log.error("----- IN TgBot :: onUpdateReceived:: Unsupported message type is received - {} -----", update);
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
