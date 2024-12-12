package com.golovkin.websocket.service;

import com.golovkin.websocket.model.ChatMessage;

import java.util.List;
import java.util.Optional;

public interface ChatMessageService {
    ChatMessage saveMessage(ChatMessage message); // Сохранить сообщение
    List<ChatMessage> getChatMessages(String senderId, String recipientId);
    void deleteMessage(String messageId);
    Optional<ChatMessage> updateMessage(String senderId, String updatedContent);
}
