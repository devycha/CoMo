package com.dongjji.como.chat.service;

import com.dongjji.como.chat.dto.ChatRoomDto;
import com.dongjji.como.chat.entity.ChatRoom;
import com.dongjji.como.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;


    public long createNewChat(String capEmail, String invitedEmail) {
        ChatRoom chatRoom = chatRoomRepository.save(
                ChatRoom.builder()
                        .capUser(capEmail)
                        .invitedUser(invitedEmail)
                        .build()
        );

        return chatRoom.getId();
    }

    public ChatRoomDto.Response verifyChatAuth(String email, long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 채팅방 입니다.")
        );

        String chatName = "(알수 없는 이름)";
        String myName = "(알수 없는 이름)";

        if (chatRoom.getCapUser().equals(email)) {
            chatName = chatRoom.getInvitedUser();
            myName = chatRoom.getCapUser();
        } else if (chatRoom.getInvitedUser().equals(email)) {
            chatName = chatRoom.getCapUser();
            myName = chatRoom.getInvitedUser();
        } else {
            throw new RuntimeException("접근 권한이 없습니다");
        }

        return ChatRoomDto.Response.builder()
                .id(chatRoomId)
                .myName(myName)
                .chatName(chatName)
                .build();
    }
}
