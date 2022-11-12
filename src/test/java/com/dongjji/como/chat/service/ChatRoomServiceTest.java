package com.dongjji.como.chat.service;

import com.dongjji.como.chat.dto.ChatRoomDto;
import com.dongjji.como.chat.entity.ChatRoom;
import com.dongjji.como.chat.exception.ChatRoomNotFoundException;
import com.dongjji.como.chat.exception.UnAuthorizedChatRoomAccessException;
import com.dongjji.como.chat.repository.ChatRepository;
import com.dongjji.como.chat.repository.ChatRoomRepository;
import com.dongjji.como.common.error.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ChatRoomServiceTest {

    @Mock
    private ChatRepository chatRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @InjectMocks
    private ChatRoomService chatRoomService;

    @Test
    @DisplayName("내 모든 채팅방 조회 성공")
    void getMyChatsSuccessTest() {
        //given
        String myEmail = "inviteUser";
        String capUser = "capUser";
        String inviteUser = "inviteUser";

        List<ChatRoom> chatRooms = new ArrayList<>();
        ChatRoom chatRoom = ChatRoom.builder()
                .id(1L)
                .capUser(capUser)
                .invitedUser(inviteUser)
                .build();

        chatRooms.add(chatRoom);
        given(chatRoomRepository.findAllByCapUserOrInvitedUser(inviteUser, inviteUser))
                .willReturn(chatRooms);

        //when
        List<ChatRoomDto.Response> myChats = chatRoomService.getMyChats(myEmail);

        //then
        assertEquals(myChats.get(0).getChatName(), capUser);
        assertEquals(myChats.size(), 1);
    }

    @Test
    @DisplayName("새로운 채팅방 생성 성공")
    void createNewChatSuccessTest() {
        //given
        long id = 1L;
        String capUser = "capUser";
        String inviteUser = "inviteUser";

        ChatRoom chatRoom = ChatRoom.builder()
                .id(id)
                .capUser(capUser)
                .invitedUser(inviteUser)
                .build();

        given(chatRoomRepository.save(any())).willReturn(chatRoom);

        //when
        long saveChatRoomId = chatRoomService.createNewChat(capUser, inviteUser);

        // then
        assertEquals(id, saveChatRoomId);
    }

    @Test
    @DisplayName("채팅방 조회 실패 : 존재하지 않는 채팅방")
    void getChatFailedByNotFoundExceptionTest() {
        //given
        given(chatRoomRepository.findById(anyLong())).willReturn(Optional.empty());

        //when
        ChatRoomNotFoundException exception = assertThrows(ChatRoomNotFoundException.class,
                () -> chatRoomService.verifyChatAuth("myEmail", 1L));

        //then
        assertEquals(exception.getMessage(), ErrorCode.CHATROOM_NOT_FOUND.getErrorMessage());
    }

    @Test
    @DisplayName("채팅방 조회 실패 : 권한 없음")
    void getChatFailedByUnAuthorizedTest() {
        Long id = 1L;
        String myEmail = "myEmail";
        String capUser = "capUser";
        String inviteUser = "notMyEmail";

        ChatRoom chatRoom = ChatRoom.builder()
                .id(id)
                .capUser(capUser)
                .invitedUser(inviteUser)
                .build();

        // given
        given(chatRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(chatRoom));

        // when
        UnAuthorizedChatRoomAccessException exception = assertThrows(UnAuthorizedChatRoomAccessException.class,
                () -> chatRoomService.verifyChatAuth(myEmail, 1L));

        // then
        assertEquals(exception.getMessage(), ErrorCode.FORBIDDEN.getErrorMessage());
    }

    @Test
    @DisplayName("채팅방 조회 성공 : 초대받은 상대일 때")
    void getChatSuccessByInviteUserTest() {
        Long id = 1L;
        String myEmail = "myEmail";
        String capUser = "capUser";
        String inviteUser = myEmail;

        ChatRoom chatRoom = ChatRoom.builder()
                .id(id)
                .capUser(capUser)
                .invitedUser(inviteUser)
                .build();

        // given
        given(chatRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(chatRoom));

        // when
        ChatRoomDto.Response response = chatRoomService.verifyChatAuth(myEmail, 1L);


        // then
        assertEquals(response.getId(), id);
        assertEquals(response.getMyName(), myEmail);
        assertEquals(response.getCapName(), capUser);
        assertEquals(response.getChatName(), capUser);
    }

    @Test
    @DisplayName("채팅방 조회 성공 : 방장일 때")
    void getChatSuccessByCapUserTest() {
        Long id = 1L;
        String myEmail = "myEmail";
        String capUser = myEmail;
        String inviteUser = "inviteUser";

        ChatRoom chatRoom = ChatRoom.builder()
                .id(id)
                .capUser(capUser)
                .invitedUser(inviteUser)
                .build();

        // given
        given(chatRoomRepository.findById(anyLong()))
                .willReturn(Optional.of(chatRoom));

        // when
        ChatRoomDto.Response response = chatRoomService.verifyChatAuth(myEmail, 1L);


        // then
        assertEquals(response.getId(), id);
        assertEquals(response.getMyName(), myEmail);
        assertEquals(response.getCapName(), myEmail);
        assertEquals(response.getChatName(), inviteUser);
    }
}