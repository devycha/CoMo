package com.dongjji.como.chat.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Document(collection = "chat")
public class Chat {
    private String id;
    private String msg;
    private String sender;
    private String receiver;
    private Integer roomNum;

    private LocalDateTime createdAt;
}
