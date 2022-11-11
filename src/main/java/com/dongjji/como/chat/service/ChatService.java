package com.dongjji.como.chat.service;

import com.dongjji.como.chat.entity.Chat;
import com.dongjji.como.chat.repository.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final ChatRepository chatRepository;

    public Flux<Chat> getPrivateChats(String sender, String receiver) {
        return chatRepository.mFindBySender(sender, receiver)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Flux<Chat> getPublicChats(Integer roomNum) {
        return chatRepository.mFindByRoomNum(roomNum)
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Chat> sendChat(Chat chat) {
        chat.setCreatedAt(LocalDateTime.now());
        return chatRepository.save(chat);
    }
}
