package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.RegStatus;
import dev.shulika.xtelworkbot.service.MessageService;
import dev.shulika.xtelworkbot.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.shulika.xtelworkbot.BotConst.BTN_CANCEL_CALLBACK;
import static dev.shulika.xtelworkbot.BotConst.BTN_START_REG_CALLBACK;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler {
    private final MessageService messageService;
    private final RegistrationService registrationService;

    public void switchCallbackQueryByType(Update update) {
        var query = update.getCallbackQuery();
        var queryData = query.getData();
        var message = query.getMessage();
//        var messageId = query.getMessage().getMessageId();
//        var queryId = query.getId();
//        var id = query.getFrom().getId();
//        var firstName = query.getFrom().getFirstName();

        switch (queryData) {
            case BTN_CANCEL_CALLBACK -> registrationService.regSwitch(message, RegStatus.CANCEL);
            case BTN_START_REG_CALLBACK -> registrationService.regSwitch(message, RegStatus.COMMON_PASS);
        }
    }
}
