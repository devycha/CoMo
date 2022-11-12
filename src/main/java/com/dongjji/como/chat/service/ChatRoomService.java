package com.dongjji.como.chat.service;

import com.dongjji.como.chat.dto.ChatRoomDto;
import com.dongjji.como.chat.entity.ChatRoom;
import com.dongjji.como.chat.exception.ChatRoomNotFoundException;
import com.dongjji.como.chat.exception.UnAuthorizedChatRoomAccessException;
import com.dongjji.como.chat.repository.ChatRoomRepository;
import com.dongjji.como.chat.type.ChatType;
import com.dongjji.como.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.dongjji.como.chat.type.ChatType.UNKNOWN;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomDto.Response> getMyChats(String email) {

        List<ChatRoomDto.Response> chats = chatRoomRepository
                .findAllByCapUserOrInvitedUser(email, email)
                .stream().map(e -> ChatRoomDto.Response.of(email, e))
                .collect(Collectors.toList());

        return chats;
    }


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
                () -> new ChatRoomNotFoundException(ErrorCode.CHATROOM_NOT_FOUND)
        );

        String chatName = UNKNOWN.getName();
        String myName = UNKNOWN.getName();

        if (chatRoom.getCapUser().equals(email)) {
            chatName = chatRoom.getInvitedUser();
            myName = chatRoom.getCapUser();
        } else if (chatRoom.getInvitedUser().equals(email)) {
            chatName = chatRoom.getCapUser();
            myName = chatRoom.getInvitedUser();
        } else {
            throw new UnAuthorizedChatRoomAccessException(ErrorCode.FORBIDDEN);
        }

        return ChatRoomDto.Response.builder()
                .id(chatRoomId)
                .myName(myName)
                .chatName(chatName)
                .capName(chatRoom.getCapUser())
                .build();
    }

    public void getOutChatRoom(long chatRoomId, String email) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 채팅방 입니다.")
        );

        if (chatRoom.getCapUser().equals(email)) {
            chatRoom.setCapUser(UNKNOWN.getName());
        } else if (chatRoom.getInvitedUser().equals(email)) {
            chatRoom.setInvitedUser(UNKNOWN.getName());
        } else {
            throw new RuntimeException("권한이 없는 접근입니다.");
        }

        chatRoomRepository.save(chatRoom);
        if (chatRoom.getCapUser().equals(UNKNOWN.getName())
                && chatRoom.getInvitedUser().equals(UNKNOWN.getName())) {
            chatRoomRepository.delete(chatRoom);
        }
    }

    public void deleteChatRoom(long chatRoomId, String email) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
                () -> new RuntimeException("존재하지 않는 채팅방 입니다.")
        );

        if (!chatRoom.getCapUser().equals(email)) {
            throw new RuntimeException("채팅방의 방장이 아닙니다.");
        }

        chatRoomRepository.delete(chatRoom);
    }
}
