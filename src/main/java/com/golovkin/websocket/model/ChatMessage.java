package com.golovkin.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document()
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    @Id
    private String id;
    private String chatId; // Идентификатор комнаты
    private String senderId; // Идентификатор отправителя
    private String recipientId; // Идентификатор получателя
    private String content; // Текст сообщения
    private Date timestamp; // Время отправки

}
