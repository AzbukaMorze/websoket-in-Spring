package com.golovkin.websocket.service;

import com.golovkin.websocket.model.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage saveMessage(ChatMessage message); // Сохранить сообщение
    List<ChatMessage> getChatMessages(String senderId, String recipientId);
}
