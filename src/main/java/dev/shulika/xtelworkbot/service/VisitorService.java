package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.Visitor;
import dev.shulika.xtelworkbot.repository.VisitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import static dev.shulika.xtelworkbot.BotConst.HELLO_MSG;
import static dev.shulika.xtelworkbot.BotConst.HELP_MSG;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class VisitorService {
    private final MessageService messageService;
    private final VisitorRepository visitorRepository;

    public void saveNewVisitor(Message message) {
        long chatId = message.getChatId();
        String firstName = message.getChat().getFirstName();
        messageService.sendResponseWithMarkDownV2(message, HELLO_MSG + firstName);
        messageService.sendResponseWithMarkDownV2(message, HELP_MSG);

        if (visitorRepository.findById(chatId).isEmpty()) {
            Visitor visitor = Visitor.builder()
                    .id(chatId)
                    .firstName(firstName)
                    .lastName(message.getChat().getLastName())
                    .userName(message.getChat().getUserName())
                    .build();
            visitorRepository.save(visitor);
            log.info("IN VisitorService :: saveNewVisitor :: ChatId - {}, FirstName - {} :: Saved", chatId, firstName);
        }
    }
}