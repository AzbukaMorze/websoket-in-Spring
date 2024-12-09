package com.golovkin.websocket.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "messages")
public class ChatMessage {
    @Id
    private String id;
    private String chatId; // Идентификатор комнаты
    private String senderId; // Идентификатор отправителя
    private String recipientId; // Идентификатор получателя
    private String content; // Текст сообщения
    private LocalDateTime timestamp; // Время отправки
    private MessageStatus status; // Статус сообщения (например, SENT, DELIVERED, READ)

    public enum MessageStatus {
        SENT,
        DELIVERED,
        READ
    }
}
