package com.dongjji.como.chat.controller;

import com.dongjji.como.chat.entity.Chat;
import com.dongjji.como.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Controller
//@RestController
public class ChatController {
    private final ChatService chatService;

//    @ResponseBody
//    @CrossOrigin
//    @GetMapping(value="/sender/{sender}/receiver/{receiver}", produces= MediaType.TEXT_EVENT_STREAM_VALUE)
//    public Flux<Chat> getPrivateMsg(@PathVariable String sender, @PathVariable String receiver) {
//        return chatService.getPrivateChats(sender, receiver);
//    }

    @ResponseBody
    @CrossOrigin
    @GetMapping(value = "/chat/{roomNum}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Chat> getPublicMsg(@PathVariable Integer roomNum) {
        return chatService.getPublicChats(roomNum);
    }

    @ResponseBody
    @CrossOrigin
    @PostMapping("/chat")
    public Mono<Chat> sendChat(@RequestBody Chat chat) {
        log.warn("chat: " + chat);
        return chatService.sendChat(chat);
    }
}
