package com.golovkin.websocket.repository;

import com.golovkin.websocket.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findByChatId(String chatId); // Найти все сообщения в комнате
    List<ChatMessage> findBySenderIdAndRecipientId(String senderId, String recipientId); // Найти сообщения между двумя пользователями
}
