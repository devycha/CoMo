package com.dongjji.como.chat.dto;

import com.dongjji.como.chat.entity.ChatRoom;
import lombok.*;

public class ChatRoomDto {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {
        private String invitedEmail;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private long id;
        private String myName;
        private String chatName;
        private String capName;

        public static Response of(String myEmail, ChatRoom chatRoom) {
            String chatName = "(알수 없는 이름)";
            String myName = "(알수 없는 이름)";

            if (chatRoom.getCapUser().equals(myEmail)) {
                chatName = chatRoom.getInvitedUser();
                myName = chatRoom.getCapUser();
            } else if (chatRoom.getInvitedUser().equals(myEmail)) {
                chatName = chatRoom.getCapUser();
                myName = chatRoom.getInvitedUser();
            }

            return Response.builder()
                    .id(chatRoom.getId())
                    .myName(myName)
                    .chatName(chatName)
                    .capName(chatRoom.getCapUser())
                    .build();
        }
    }
}

