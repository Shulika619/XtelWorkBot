package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.AppUser;
import dev.shulika.xtelworkbot.model.RegStatus;
import dev.shulika.xtelworkbot.model.Role;
import dev.shulika.xtelworkbot.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.ws.rs.NotFoundException;

import static dev.shulika.xtelworkbot.BotConst.HELLO_MSG;
import static dev.shulika.xtelworkbot.BotConst.HELP_MSG;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AppUserService {
    private final MessageService messageService;
    private final AppUserRepository appUserRepository;

    public void saveNewAppUser(Message message) {
        long chatId = message.getChatId();
        String firstName = message.getChat().getFirstName();
        messageService.sendMessage(message, HELLO_MSG + firstName + "\\!\n\n" + HELP_MSG);

        if (appUserRepository.findById(chatId).isEmpty()) {
            AppUser appUser = AppUser.builder()
                    .id(chatId)
                    .tgFirstName(firstName)
                    .tgLastName(message.getChat().getLastName())
                    .tgUserName(message.getChat().getUserName())
                    .regStatus(RegStatus.NONE)
                    .role(Role.USER)
                    .isRegistered(false)
                    .build();
            appUserRepository.save(appUser);
            log.info("IN AppUserService :: saveNewAppUser :: ChatId - {}, FirstName - {} :: Saved", chatId, firstName);
        }
    }

    public AppUser findUserById(long chatId){
        return appUserRepository.findById(chatId).orElse(null);
    }

    public void changeState(Message message, RegStatus regStatus){
        var user = appUserRepository.findById(message.getChatId())
                .orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        user.setRegStatus(regStatus);
        log.info("+++++ IN AppUserService :: changeState :: ChatId - {}, FirstName - {}, Status - {} :: Saved",
                message.getChatId(), message.getChat().getFirstName(), regStatus);
    }

    public void setFullName(Message message){
        var user = appUserRepository.findById(message.getChatId())
                .orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        var newFullName = message.getText();
        user.setFullName(newFullName);
        log.info("+++++ IN AppUserService :: setFullName :: ChatId - {}, FirstName - {}, FullName - {} :: Saved",
                message.getChatId(), message.getChat().getFirstName(), newFullName);
    }
}