package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.TgBot;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class ExecuteMessageService {
    private TgBot tgBot;
    @Autowired
    public void setTgBot(TgBot tgBot) {
        this.tgBot = tgBot;
    }



    public void execute(SendMessage sendMessage) {
        try {
            tgBot.execute(sendMessage);
            log.info("++++++ IN MessageSender :: sendMessage executed :: chatId - {} :: text - {}", sendMessage.getChatId(), sendMessage.getText());
        } catch (TelegramApiException e) {
            log.error("----- IN MessageSender :: sendMessage FAIL -----");
        }
    }
}
