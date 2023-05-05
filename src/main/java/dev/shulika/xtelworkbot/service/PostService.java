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
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static dev.shulika.xtelworkbot.BotConst.*;

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
                        .callbackData(BTN_ACCEPT_TXT_TASK_CALLBACK + ":" + postId)
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
                InlineKeyboardButton.builder()
                        .text(BTN_ACCEPT_TASK)
                        .callbackData(BTN_ACCEPT_PHOTO_TASK_CALLBACK + ":" + postId)
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
                InlineKeyboardButton.builder()
                        .text(BTN_ACCEPT_TASK)
                        .callbackData(BTN_ACCEPT_DOC_TASK_CALLBACK + ":" + postId)
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

    public boolean sendPhotoPostNewExecutor(Long postId) {
        log.info("+++++ IN PostService :: sendPhotoPostNewExecutor :: ID - {} :: START", postId);
        var post = postRepository.getById(postId);
        var employeeList = post.getToDepartment().getEmployees();
        var sendMsg = postTxtTemplate(post);
        for (Employee employee : employeeList) {
            messageService.sendPhotoMessageToDepartment(employee.getId(), sendMsg, post.getFileId());
        }
        log.info("+++++ IN PostService :: sendPhotoPostNewExecutor :: ID - {}, EmployeeFullName - {}, SendToDepartment - {}, TextMsg - {} :: COMPLETE",
                postId, post.getFromEmployee().getFullName(), post.getToDepartment().getName(), post.getTextMsg());
        return true;
    }

    public boolean sendDocPostNewExecutor(Long postId) {
        log.info("+++++ IN PostService :: sendDocPostNewExecutor :: ID - {} :: START", postId);
        var post = postRepository.getById(postId);
        var employeeList = post.getToDepartment().getEmployees();
        var sendMsg = postTxtTemplate(post);
        for (Employee employee : employeeList) {
            messageService.sendDocMessageToDepartment(employee.getId(), sendMsg, post.getFileId());
        }
        log.info("+++++ IN PostService :: sendDocPostNewExecutor :: ID - {}, EmployeeFullName - {}, SendToDepartment - {}, TextMsg - {} :: COMPLETE",
                postId, post.getFromEmployee().getFullName(), post.getToDepartment().getName(), post.getTextMsg());
        return true;
    }

    public void taskListDepartment(Message message, long departmentId, int interval) {
        var posts = postRepository.findAllPostByDepartmentId(departmentId, interval);
        System.out.println(" ================================== SIZE2 - " + posts.size());  // TODO: delete

        var dateMinusInterval = Instant.now().minus(Duration.ofDays(interval));
        var date = new SimpleDateFormat("dd/MM/yyyy").format(Timestamp.from(dateMinusInterval));

        if (posts.isEmpty()) {
            var inlineKeyboardMarkup = getInlineKeyboardMarkup(departmentId);
            var msgTxt = String.format("%s: %s",SEND_MSG_EMPTY_TASKS, date);
            messageService.sendEditInlineKeyboardMarkup(message, msgTxt, inlineKeyboardMarkup);
            return;
        }

        var departmentName = posts.get(0).getToDepartment().getName();
        var sendMsg = new StringBuilder();
        sendMsg.append(String.format("%s: %s \\- %s\n", SEND_MSG_TASKS, date, departmentName));
        for (Post post : posts) {
            sendMsg.append(String.format("\n\uD83D\uDCE9 *№ %d* \uD83D\uDCE9\n", post.getId()));
            sendMsg.append(String.format("_От:_ *%s \\(%s\\)* ", post.getFromEmployee().getFullName(), post.getFromEmployee().getTgFirstName()));
            sendMsg.append(post.getFromEmployee().getDepartment().getName());
            sendMsg.append(String.format("\n_Кому:_ *%s*", post.getToDepartment().getName()));
            if (post.getTaskExecutor() != null)
                sendMsg.append(String.format("\n_Исполнитель:_ *%s \\(%s\\)*", post.getTaskExecutor().getFullName(), post.getTaskExecutor().getTgFirstName()));
            sendMsg.append(String.format("\n\uD83D\uDCAC_Тема:_ `%s`\n", post.getTextMsg()));
        }
        var inlineKeyboardMarkup = getInlineKeyboardMarkup(departmentId);
        messageService.sendEditInlineKeyboardMarkup(message, sendMsg.toString(), inlineKeyboardMarkup);
    }

    private InlineKeyboardMarkup getInlineKeyboardMarkup(Long departmentId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        keyboard.add(List.of(
                InlineKeyboardButton.builder()
                        .text("Вчера")
                        .callbackData(BTN_TASK_LIST_YESTERDAY_CALLBACK + ":" + departmentId)
                        .build(),
                InlineKeyboardButton.builder()
                        .text("2 дня назад")
                        .callbackData(BTN_TASK_LIST_2DAYS_CALLBACK + ":" + departmentId)
                        .build(),
                InlineKeyboardButton.builder()
                        .text("3 дня назад")
                        .callbackData(BTN_TASK_LIST_3DAYS_CALLBACK + ":" + departmentId)
                        .build()
        ));
        inlineKeyboardMarkup.setKeyboard(keyboard);
        return inlineKeyboardMarkup;
    }

}