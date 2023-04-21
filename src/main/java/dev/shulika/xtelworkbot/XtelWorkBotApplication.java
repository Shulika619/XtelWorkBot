package dev.shulika.xtelworkbot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@Slf4j
public class XtelWorkBotApplication {

    public static void main(String[] args) {

        SpringApplication.run(XtelWorkBotApplication.class, args);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new TgBot());
            log.info("+++++ IN XtelWorkBotApplication :: registerBot +++++");
        } catch (TelegramApiException e) {
            log.error("----- IN XtelWorkBotApplication :: registerBot FAIL -----");
        }
    }
}
