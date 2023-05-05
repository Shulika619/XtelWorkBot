package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.controller.TgBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static dev.shulika.xtelworkbot.BotConst.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageService {
    private final TgBot tgBot;

    public void sendMessage(Message message, String sendText) {
        var sendMsg = SendMessage.builder()
                .text(sendText)
                .parseMode(ParseMode.MARKDOWNV2)
                .chatId(message.getChatId())
                .build();
        executeSendMsg(sendMsg);
    }

    public void sendEditMessage(Message message, String sendText) {
        var sendEditMsg = EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text(sendText)
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        executeEditMsg(sendEditMsg);
    }

    public void sendInlineKeyboardMarkup(Message message, String sendText, InlineKeyboardMarkup inlineKeyboardMarkup) {
        var sendMsg = SendMessage.builder()
                .text(sendText)
                .parseMode(ParseMode.MARKDOWNV2)
                .chatId(message.getChatId())
                .build();
        sendMsg.setReplyMarkup(inlineKeyboardMarkup);
        executeSendMsg(sendMsg);
    }

    public void sendEditInlineKeyboardMarkup(Message message, String sendText, InlineKeyboardMarkup inlineKeyboardMarkup) {
        var sendEditMsg = EditMessageText.builder()
                .chatId(message.getChatId())
                .messageId(message.getMessageId())
                .text(sendText)
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        sendEditMsg.setReplyMarkup(inlineKeyboardMarkup);
        executeEditMsg(sendEditMsg);
    }

    public void sendMessageToDepartment(Long chatId, String sendText, InlineKeyboardMarkup inlineKeyboardMarkup) {
        var sendMsg = SendMessage.builder()
                .text(sendText)
                .chatId(chatId)
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        sendMsg.setReplyMarkup(inlineKeyboardMarkup);
        executeSendMsg(sendMsg);
    }
    public void sendMessageToDepartment(Long chatId, String sendText) {
        var sendMsg = SendMessage.builder()
                .text(sendText)
                .chatId(chatId)
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        executeSendMsg(sendMsg);
    }

    public void sendPhotoMessageToDepartment(Long chatId, String sendText, InlineKeyboardMarkup inlineKeyboardMarkup, String fileId) {
        var sendPhotoMsg = SendPhoto.builder()
                .chatId(chatId)
                .caption(sendText)
                .photo(new InputFile(fileId))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        sendPhotoMsg.setReplyMarkup(inlineKeyboardMarkup);
        executeSendPhotoMsg(sendPhotoMsg);
    }
    public void sendDocMessageToDepartment(Long chatId, String sendText, InlineKeyboardMarkup inlineKeyboardMarkup, String fileId) {
        var sendDocMsg = SendDocument.builder()
                .chatId(chatId)
                .caption(sendText)
                .document(new InputFile(fileId))
                .parseMode(ParseMode.MARKDOWNV2)
                .build();
        sendDocMsg.setReplyMarkup(inlineKeyboardMarkup);
        executeSendDocMsg(sendDocMsg);
    }

    public void deleteMsg(Message message) {
        var delMsg = new DeleteMessage();
        delMsg.setChatId(message.getChatId());
        delMsg.setMessageId(message.getMessageId());
        executeDeleteMsg(delMsg);
    }

    public InlineKeyboardButton createCancelButton() {
        return InlineKeyboardButton.builder()
                .text(BTN_CANCEL)
                .callbackData(BTN_CANCEL_CALLBACK + ":" + null)
                .build();
    }

    public void processed(Message message) {
        // TODO: remove if dont need
        sendMessage(message, PROCESSED_MSG);
    }


    // TODO: refactoring all execute methods
    private void executeSendMsg(SendMessage sendMessage) {
        try {
            tgBot.execute(sendMessage);
            log.info("+++++ IN MessageService :: sendMessage executed :: chatId - {} :: text - {}",
                    sendMessage.getChatId(), sendMessage.getText());
        } catch (TelegramApiException e) {
            log.error("----- IN MessageService :: sendMessage execute FAIL :: message - {}", e.getMessage());
        }
    }

    private void executeEditMsg(EditMessageText editMessageText) {
        try {
            tgBot.execute(editMessageText);
            log.info("+++++ IN MessageService :: sendEditMessage executeEditMsg :: chatId - {} :: text - {}",
                    editMessageText.getChatId(), editMessageText.getText());
        } catch (TelegramApiException e) {
            log.error("----- IN MessageService :: sendEditMessage executeEditMsg FAIL :: message - {}", e.getMessage());
        }
    }

    private void executeDeleteMsg(DeleteMessage deleteMessage) {
        try {
            tgBot.execute(deleteMessage);
            log.info("+++++ IN MessageService :: executeDeleteMsg :: chatId - {} :: msgId - {}",
                    deleteMessage.getChatId(), deleteMessage.getMessageId());
        } catch (TelegramApiException e) {
            log.error("----- IN MessageService :: executeDeleteMsg FAIL :: message - {}", e.getMessage());
        }
    }

    private void executeSendPhotoMsg(SendPhoto sendPhoto) {
        try {
            tgBot.execute(sendPhoto);
            log.info("+++++ IN MessageService :: executeSendPhotoMsg executed :: chatId - {} :: text - {}",
                    sendPhoto.getChatId(), sendPhoto.getCaption());
        } catch (TelegramApiException e) {
            log.error("----- IN MessageService :: executeSendPhotoMsg execute FAIL :: message - {}", e.getMessage());
        }
    }
    private void executeSendDocMsg(SendDocument sendDocument) {
        try {
            tgBot.execute(sendDocument);
            log.info("+++++ IN MessageService :: executeSendDocMsg executed :: chatId - {} :: text - {}",
                    sendDocument.getChatId(), sendDocument.getCaption());
        } catch (TelegramApiException e) {
            log.error("----- IN MessageService :: executeSendDocMsg execute FAIL :: message - {}", e.getMessage());
        }
    }

//    public void test1(Message message) {
//        var sendMessage = SendMessage.builder()
//                .text("<b>Bold</b> " +
//                      "<i>italic</i>" +
//                      " <code>mono</code> " +
//                      "<a href=\"google.com\">Google</a>")
//                .parseMode("HTML")
//                .chatId(message.getChatId())
//                .build();
//        execute(sendMessage);
//    }
//
//    public void test2(Message message) {
//        var markup = new ReplyKeyboardMarkup();
//        var keyboardRows = new ArrayList<KeyboardRow>();
//        KeyboardRow row1 = new KeyboardRow();
//        KeyboardRow row2 = new KeyboardRow();
//        KeyboardRow row3 = new KeyboardRow();
//        row1.add("Button 1");
//        row1.add("Button 2");
//        row1.add("Button 3");
//        row2.add(KeyboardButton.builder().text("Phone Number")
//                .requestContact(true)
//                .build());
//        row3.add(KeyboardButton.builder()
//                .requestLocation(true)
//                .text("Location")
//                .build());
//        keyboardRows.add(row1);
//        keyboardRows.add(row2);
//        keyboardRows.add(row3);
//        markup.setKeyboard(keyboardRows);
//        markup.setResizeKeyboard(true);
//        markup.setOneTimeKeyboard(true);
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setText("Test");
//        sendMessage.setChatId(message.getChatId());
//        sendMessage.setReplyMarkup(markup);
//        execute(sendMessage);
//        System.out.println("+++++++++ test2 Extcute now TODO");
//    }

//    public void test3(Message message) {
//        SendMessage sendMessage = new SendMessage();
//        sendMessage.setChatId(message.getChatId());
//        sendMessage.setText(REGISTRATION_MSG_TITLE);
//
//        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
//        keyboard.add(
//                List.of(
//                        InlineKeyboardButton.builder()
//                                .text("Text button btn1")
//                                .callbackData("btn1")
//                                .build(),
//                        InlineKeyboardButton.builder()
//                                .text("Text button btn2")
//                                .callbackData("btn2")
//                                .build()
//                ));
//        inlineKeyboardMarkup.setKeyboard(keyboard);
//
//        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
//        execute(sendMessage);
//    }

//    public void test4(Message message) {
//        Integer messageId = callbackQuery.getMessage().getMessageId();
//        var editMessageText = new EditMessageText();
//        editMessageText.setChatId(String.valueOf(callbackQuery.getMessage().getChatId()));
//        editMessageText.setMessageId(messageId);
//        editMessageText.setText(poemText);
//        editMessageText.setReplyMarkup(
//                InlineKeyboardMarkup.builder()
//                        .keyboardRow(
//                                Collections.singletonList(
//                                        InlineKeyboardButton.builder()
//                                                .text("Текст")
//                                                .url("http://google.com")
//                                                .build()
//                                )).build());
//        messageSender.sendEditMessage(editMessageText);
//    }

}
