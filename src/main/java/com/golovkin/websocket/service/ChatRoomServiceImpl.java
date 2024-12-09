package com.golovkin.websocket.service;

import com.golovkin.websocket.model.ChatRoom;
import com.golovkin.websocket.repository.ChatRoomRepository;
import com.golovkin.websocket.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomServiceImpl implements ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Override
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        // Проверяем, существует ли уже такая комната
        if (existsByUsers(chatRoom.getSenderId(), chatRoom.getRecipientId())) {
            throw new IllegalArgumentException("ChatRoom already exists between these users.");
        }

        // Генерируем уникальный chatId и сохраняем комнату
        chatRoom.setChatId(generateChatId(chatRoom.getSenderId(), chatRoom.getRecipientId()));
        return chatRoomRepository.save(chatRoom);
    }

    @Override
    public Optional<ChatRoom> findById(String id) {
        return chatRoomRepository.findById(id);
    }

    @Override
    public Optional<ChatRoom> findByChatId(String chatId) {
        return chatRoomRepository.findByChatId(chatId);
    }


    @Override
    public boolean existsByUsers(String senderId, String recipientId) {
        // Проверяем существование комнаты в обоих направлениях
        return chatRoomRepository.existsBySenderIdAndRecipientId(senderId, recipientId) ||
                chatRoomRepository.existsBySenderIdAndRecipientId(recipientId, senderId);
    }

    @Override
    public void deleteById(String id) {
        chatRoomRepository.deleteById(id);
    }

    public ChatRoom updateChatRoom(String id, ChatRoom updatedChatRoom) {
        // Находим существующую комнату по ID
        Optional<ChatRoom> existingChatRoomOpt = chatRoomRepository.findById(id);

        if (existingChatRoomOpt.isEmpty()) {
            throw new IllegalArgumentException("ChatRoom with ID " + id + " does not exist.");
        }

        // Обновляем поля существующей комнаты
        ChatRoom existingChatRoom = existingChatRoomOpt.get();
        existingChatRoom.setSenderId(updatedChatRoom.getSenderId());
        existingChatRoom.setRecipientId(updatedChatRoom.getRecipientId());
        existingChatRoom.setChatId(generateChatId(updatedChatRoom.getSenderId(), updatedChatRoom.getRecipientId()));

        // Сохраняем обновленную комнату
        return chatRoomRepository.save(existingChatRoom);
    }

    private String generateChatId(String senderId, String recipientId) {
        // Генерация уникального chatId
        return senderId.compareTo(recipientId) < 0
                ? senderId + "_" + recipientId
                : recipientId + "_" + senderId;
    }

    @Override
    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists
    ) {
        return chatRoomRepository
                .findBySenderIdOrRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                    if(createNewRoomIfNotExists) {
                        var chatId = createChatId(senderId, recipientId);
                        return Optional.of(chatId);
                    }

                    return  Optional.empty();
                });
    }

    @Override
    public String createChatId(String senderId, String recipientId) {
        var chatId = String.format("%s_%s", senderId, recipientId);

        ChatRoom senderRecipient = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom
                .builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
