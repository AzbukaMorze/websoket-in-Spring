package com.golovkin.websocket.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {

    @Id
    private String id;

    private String chatId;
    private String senderId;
    private String recipientId;

}
