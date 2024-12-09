package com.golovkin.websocket.service;

import com.golovkin.websocket.model.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {
    ChatRoom createChatRoom(ChatRoom chatRoom);
    Optional<ChatRoom> findById(String id);
    Optional<ChatRoom> findByChatId(String chatId);
    boolean existsByUsers(String senderId, String recipientId); // Проверка существования комнаты
    void deleteById(String id);

    String createChatId(String senderId, String recipientId);
    Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists);
}
