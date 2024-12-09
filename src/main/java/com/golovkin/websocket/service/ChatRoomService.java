package com.golovkin.websocket.service;

import com.golovkin.websocket.model.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomService {

    String createChatId(String senderId, String recipientId);
    Optional<String> getChatRoomId(String senderId, String recipientId, boolean createNewRoomIfNotExists);
}
