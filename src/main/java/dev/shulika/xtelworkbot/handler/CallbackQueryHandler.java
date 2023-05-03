package dev.shulika.xtelworkbot.handler;

import dev.shulika.xtelworkbot.model.State;
import dev.shulika.xtelworkbot.service.AppUserService;
import dev.shulika.xtelworkbot.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static dev.shulika.xtelworkbot.BotConst.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CallbackQueryHandler {
    private final MessageService messageService;
    private final RegistrationHandler registrationHandler;
    private final AppUserService appUserService;
    private final SendHandler sendHandler;

    public void switchCallbackQueryByType(Update update) {
        var query = update.getCallbackQuery();
        var queryData = query.getData();
        var message = query.getMessage();
        String[] param = queryData.split(":");
        String action = param[0];
        String value = param[1];

//        var messageId = query.getMessage().getMessageId();
//        var queryId = query.getId();
//        var id = query.getFrom().getId();
//        var firstName = query.getFrom().getFirstName();

        switch (action) {
            case BTN_CANCEL_CALLBACK -> appUserService.cancelBtn(message);
            case BTN_START_REG_CALLBACK -> registrationHandler.regSwitch(message, State.COMMON_PASS);
            case BTN_DEPARTMENT_REG_CALLBACK -> registrationHandler.checkSelectDepartmentStep6(message, value);
            case BTN_DEPARTMENT_SEND_CALLBACK -> sendHandler.checkSelectDepartmentStep2(message, value);
//            case BTN_CANCEL_TASK_CALLBACK -> sendHandler.changeSendMsgStatusCancel(message, value);
            case BTN_ACCEPT_TASK_CALLBACK -> sendHandler.changeSendMsgStatusAccept(message, value);
        }
    }
}
