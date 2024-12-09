package com.golovkin.websocket.service;

import com.golovkin.websocket.model.ChatMessage;

import java.util.List;

public interface ChatMessageService {
    ChatMessage saveMessage(ChatMessage message); // Сохранить сообщение
    List<ChatMessage> getMessagesByChatId(String chatId); // Получить сообщения по ID комнаты
    ChatMessage updateMessageStatus(String messageId, ChatMessage.MessageStatus status); // Обновить статус сообщения
    void deleteMessageById(String id); // Удалить сообщение по ID
    List<ChatMessage> getChatMessages(String senderId, String recipientId);
}
