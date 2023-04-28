package dev.shulika.xtelworkbot.service;

import dev.shulika.xtelworkbot.model.Department;
import dev.shulika.xtelworkbot.model.Employee;
import dev.shulika.xtelworkbot.model.Post;
import dev.shulika.xtelworkbot.repository.AppUserRepository;
import dev.shulika.xtelworkbot.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.ws.rs.NotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final AppUserRepository appUserRepository;
    private final MessageService messageService;

    public Long createPost(Message message) {
        log.info("+++++ IN PostService :: createPost :: ChatId - {} :: START +++++", message.getChatId());
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
        log.info("+++++ IN PostService :: createPost :: ChatId - {} IdPost - {}:: COMPLETE +++++",
                message.getChatId(), savedPost.getId());
        return savedPost.getId();
    }

    public boolean sendPost(Long postId) {
        log.info("+++++ IN PostService :: sendPost :: ID - {} :: START", postId);

        var post = postRepository.getById(postId);
        var employeeList = post.getToDepartment().getEmployees();
        if (employeeList.isEmpty()) {
            log.error("----- IN PostService :: sendPost FAIL - Employees is Empty :: PostId - {}", postId);
            return false;
        }

        var sendMsg = new StringBuilder();
        sendMsg.append("Отправитель: " + post.getFromEmployee().getFullName());
        sendMsg.append("\nПолучатели: " + post.getToDepartment().getName());
        sendMsg.append("\nТекст: " + post.getTextMsg());

        for (Employee employee : employeeList) {
            messageService.sendMessageToDepartment(employee.getId(), sendMsg.toString());
            System.out.println("===================== " + employee.getId());
        }

        log.info("+++++ IN PostService :: sendPost :: ID - {}, EmployeeFullName - {}, SendToDepartment - {}, TextMsg - {} :: COMPLETE",
                post.getId(), post.getFromEmployee().getFullName(), post.getToDepartment().getName(), post.getTextMsg());
        return true;
    }

}