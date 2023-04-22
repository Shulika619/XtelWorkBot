package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler {
    private final MessageService messageService;
    public void switchCallbackQueryByType(Update update) {

    }
}
