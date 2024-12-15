package com.golovkin.websocket.service;

import com.golovkin.websocket.exceptions.ChatRoomNotFoundException;
import com.golovkin.websocket.model.ChatMessage;
import com.golovkin.websocket.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatMessageServiceImp implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomServiceImpl chatRoomService;

    @Override
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true)
                .orElseThrow(() -> new ChatRoomNotFoundException(
                        String.format("Chat room not found for senderId %s and recipientId %s",
                                chatMessage.getSenderId(), chatMessage.getRecipientId())
                ));
        chatMessage.setChatId(chatId);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }


    @Override
    public List<ChatMessage> getChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomService.getChatRoomId(senderId, recipientId, false);
        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }

    @Override
    public Optional<ChatMessage> updateMessage(String messageId, String updatedContent) {
        ChatMessage message = chatMessageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found")); // Добавьте своё исключение
        message.setContent(updatedContent);
        return Optional.of(chatMessageRepository.save(message));
    }

    @Override
    public void deleteMessage(String messageId) {
        chatMessageRepository.deleteById(messageId);
    }
}
