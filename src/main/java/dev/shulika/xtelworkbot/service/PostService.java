package dev.shulika.xtelworkbot.service;

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

    public void createPost(Message message){
        log.info("+++++ IN PostService :: createPost :: ChatId - {} :: START +++++", message.getChatId());
        var appUser = appUserRepository.findById(message.getChatId()).
                orElseThrow(() -> new NotFoundException("----- User Not found-----"));
        var newPost = Post.builder()
                .employeeId(message.getChatId())
                .departmentId(appUser.getSendTo())
                .textMsg(message.getText())
                .build();
        var savedPost = postRepository.save(newPost);
        log.info("+++++ IN PostService :: createPost :: ChatId - {} IdPost - {}:: COMPLETE +++++",
                message.getChatId(), savedPost.getId());
        sendPost(savedPost);
    }

    public void sendPost(Post post){
        log.info("+++++ IN PostService :: sendPost :: ID - {} :: START", post.getId());

        System.out.println("================== ЦИКЛ отправки сообщений ===============");


        log.info("+++++ IN PostService :: sendPost :: ID - {}, EmployeeId - {}, SendToDepartmentId - {}, TextMsg - {} :: COMPLETE",
                post.getId(),post.getEmployeeId(),post.getDepartmentId(), post.getTextMsg());
    }

}