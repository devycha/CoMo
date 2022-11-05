package com.dongjji.como.chat.dto;

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
    }
}
