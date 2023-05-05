package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.Department;
import dev.shulika.xtelworkbot.model.Employee;
import dev.shulika.xtelworkbot.model.Post;
import dev.shulika.xtelworkbot.repository.AppUserRepository;
import dev.shulika.xtelworkbot.repository.EmployeeRepository;
import dev.shulika.xtelworkbot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.shulika.xtelworkbot.BotConst.BTN_ACCEPT_TASK;
import static dev.shulika.xtelworkbot.BotConst.BTN_ACCEPT_TASK_CALLBACK;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;
    private final MessageService messageService;
    private final EmployeeRepository employeeRepository;


    private String postTxtTemplate(Post post) {
        var sendMsg = new StringBuilder();
        if (post.getTaskExecutor() == null) {
            sendMsg.append(String.format("\uD83D\uDCE9 *Новое уведомление № __%d__* \uD83D\uDCE9", post.getId()));
        } else {
            sendMsg.append(String.format("\uD83D\uDCCC *Задание № __%d__ новый исполнитель* \uD83D\uDCCC", post.getId()));
            sendMsg.append(String.format("\n\n_Исполнитель:_ *%s \\(%s\\)*", post.getTaskExecutor().getFullName(), post.getTaskExecutor().getTgFirstName()));
        }
        sendMsg.append(String.format("\n\n_От:_ *%s \\(%s\\)*", post.getFromEmployee().getFullName(), post.getFromEmployee().getTgFirstName()));
        sendMsg.append(String.format("\n_Кому:_ *%s*", post.getToDepartment().getName()));
        sendMsg.append(String.format("\n\n\uD83D\uDCAC_Тема:_ `%s`", post.getTextMsg()));
        sendMsg.append("\n\n _\\*текст темы можно скопировать нажав на него_");
        return sendMsg.toString();
    }



    public Long createTxtPost(Message message) {
        log.info("+++++ IN PostService :: createTxtPost :: ChatId - {} :: START +++++", message.getChatId());
        var appUser = appUserRepository.findById(message.getChatId()).
                orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        var newPost = Post.builder()
                .fromEmployee(Employee.builder()
                        .id(message.getChatId())
                        .build()
                )
                .toDepartment(Department.builder()
                        .id(appUser.getSendTo())
                        .build()
                )
                .textMsg(message.getText())
                .build();
        var savedPost = postRepository.save(newPost);
        log.info("+++++ IN PostService :: createTxtPost :: ChatId - {} IdPost - {}:: COMPLETE +++++",
                message.getChatId(), savedPost.getId());
        return savedPost.getId();
    }

    public boolean sendTxtPost(Long postId) {
        log.info("+++++ IN PostService :: sendPost :: ID - {} :: START", postId);
        var post = postRepository.getById(postId);
        var employeeList = post.getToDepartment().getEmployees();
        if (employeeList.isEmpty()) {
            log.error("----- IN PostService :: sendPost FAIL - Employees is Empty :: PostId - {}", postId);
            return false;
        }
        var sendMsg = postTxtTemplate(post);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
//                InlineKeyboardButton.builder()
//                        .text(BTN_CANCEL_TASK)
//                        .callbackData(BTN_CANCEL_TASK_CALLBACK + ":" + postId)
//                        .build(),
                InlineKeyboardButton.builder()
                        .text(BTN_ACCEPT_TASK)
                        .callbackData(BTN_ACCEPT_TASK_CALLBACK + ":" + postId)
                        .build()
        ));
        inlineKeyboardMarkup.setKeyboard(keyboard);

        for (Employee employee : employeeList) {
            messageService.sendMessageToDepartment(employee.getId(), sendMsg, inlineKeyboardMarkup);
        }
        log.info("+++++ IN PostService :: sendPost :: ID - {}, EmployeeFullName - {}, SendToDepartment - {}, TextMsg - {} :: COMPLETE",
                postId, post.getFromEmployee().getFullName(), post.getToDepartment().getName(), post.getTextMsg());
        return true;
    }

    public Long createPhotoPost(Message message) {
        log.info("+++++ IN PostService :: createPhotoPost :: ChatId - {} :: START +++++", message.getChatId());
        var appUser = appUserRepository.findById(message.getChatId()).
                orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        var newPost = Post.builder()
                .fromEmployee(Employee.builder()
                        .id(message.getChatId())
                        .build()
                )
                .toDepartment(Department.builder()
                        .id(appUser.getSendTo())
                        .build()
                )
                .textMsg(message.getCaption())
                .fileId(message.getPhoto().get(0).getFileId())
                .build();
        var savedPost = postRepository.save(newPost);
        log.info("+++++ IN PostService :: createPhotoPost :: ChatId - {} IdPost - {} :: COMPLETE +++++",
                message.getChatId(), savedPost.getId());
        return savedPost.getId();
    }

    public boolean sendPhotoPost(Long postId) {
        log.info("+++++ IN PostService :: sendPhotoPost :: ID - {} :: START", postId);
        var post = postRepository.getById(postId);
        var employeeList = post.getToDepartment().getEmployees();
        if (employeeList.isEmpty()) {
            log.error("----- IN PostService :: sendPhotoPost FAIL - Employees is Empty :: PostId - {}", postId);
            return false;
        }
        var sendMsg = postTxtTemplate(post);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
//                InlineKeyboardButton.builder()
//                        .text(BTN_CANCEL_TASK)
//                        .callbackData(BTN_CANCEL_TASK_CALLBACK + ":" + postId)
//                        .build(),
                InlineKeyboardButton.builder()
                        .text(BTN_ACCEPT_TASK)
                        .callbackData(BTN_ACCEPT_TASK_CALLBACK + ":" + postId)
                        .build()
        ));
        inlineKeyboardMarkup.setKeyboard(keyboard);

        for (Employee employee : employeeList) {
            messageService.sendPhotoMessageToDepartment(employee.getId(), sendMsg, inlineKeyboardMarkup, post.getFileId());
        }
        log.info("+++++ IN PostService :: sendPhotoPost :: ID - {}, EmployeeFullName - {}, SendToDepartment - {}, TextMsg - {} :: COMPLETE",
                postId, post.getFromEmployee().getFullName(), post.getToDepartment().getName(), post.getTextMsg());
        return true;
    }

    public Long createDocPost(Message message) {
        log.info("+++++ IN PostService :: createDocPost :: ChatId - {} :: START +++++", message.getChatId());
        var appUser = appUserRepository.findById(message.getChatId()).
                orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        var newPost = Post.builder()
                .fromEmployee(Employee.builder()
                        .id(message.getChatId())
                        .build()
                )
                .toDepartment(Department.builder()
                        .id(appUser.getSendTo())
                        .build()
                )
                .textMsg(message.getCaption())
                .fileId(message.getDocument().getFileId())
                .build();
        var savedPost = postRepository.save(newPost);
        log.info("+++++ IN PostService :: createDocPost :: ChatId - {} IdPost - {} :: COMPLETE +++++",
                message.getChatId(), savedPost.getId());
        return savedPost.getId();
    }

    public boolean sendDocPost(Long postId) {
        log.info("+++++ IN PostService :: sendDocPost :: ID - {} :: START", postId);
        var post = postRepository.getById(postId);
        var employeeList = post.getToDepartment().getEmployees();
        if (employeeList.isEmpty()) {
            log.error("----- IN PostService :: sendDocPost FAIL - Employees is Empty :: PostId - {}", postId);
            return false;
        }
        var sendMsg = postTxtTemplate(post);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(Collections.singletonList(
//                InlineKeyboardButton.builder()
//                        .text(BTN_CANCEL_TASK)
//                        .callbackData(BTN_CANCEL_TASK_CALLBACK + ":" + postId)
//                        .build(),
                InlineKeyboardButton.builder()
                        .text(BTN_ACCEPT_TASK)
                        .callbackData(BTN_ACCEPT_TASK_CALLBACK + ":" + postId)
                        .build()
        ));
        inlineKeyboardMarkup.setKeyboard(keyboard);

        for (Employee employee : employeeList) {
            messageService.sendDocMessageToDepartment(employee.getId(), sendMsg, inlineKeyboardMarkup, post.getFileId());
        }
        log.info("+++++ IN PostService :: sendDocPost :: ID - {}, EmployeeFullName - {}, SendToDepartment - {}, TextMsg - {} :: COMPLETE",
                postId, post.getFromEmployee().getFullName(), post.getToDepartment().getName(), post.getTextMsg());
        return true;
    }

    public boolean changeTaskExecutor(Long postId, Long chatId) {
        log.info("+++++ IN PostService :: changeTaskExecutor :: ID - {} :: START", postId);
        var post = postRepository.getById(postId);
        if (post.getTaskExecutor() != null) {
            log.info("----- IN PostService :: changeTaskExecutor :: ID - {} :: FAIL - Task Executor not null", postId);
            return false;
        }
        var employee = employeeRepository.getById(chatId);
        post.setTaskExecutor(employee);
        log.info("+++++ IN PostService :: changeTaskExecutor :: ID - {} :: COMPLETE", postId);
        return true;
    }

    public boolean sendPostNewExecutor(Long postId) {
        log.info("+++++ IN PostService :: sendPostNewExecutor :: ID - {} :: START", postId);
        var post = postRepository.getById(postId);
        var employeeList = post.getToDepartment().getEmployees();

        var sendMsg = postTxtTemplate(post);

        for (Employee employee : employeeList) {
            messageService.sendMessageToDepartment(employee.getId(), sendMsg);
        }
        log.info("+++++ IN PostService :: sendPostNewExecutor :: ID - {}, EmployeeFullName - {}, SendToDepartment - {}, TextMsg - {} :: COMPLETE",
                postId, post.getFromEmployee().getFullName(), post.getToDepartment().getName(), post.getTextMsg());
        return true;
    }
}