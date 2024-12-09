package com.golovkin.websocket.service;

import com.golovkin.websocket.model.ChatMessage;
import com.golovkin.websocket.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
                .orElseThrow(); // You can create your own dedicated exception
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
    public List<ChatMessage> getMessagesByChatId(String chatId) {
        // Получаем список сообщений в комнате
        return chatMessageRepository.findByChatId(chatId);
    }

    @Override
    public ChatMessage updateMessageStatus(String messageId, ChatMessage.MessageStatus status) {
        // Обновляем статус сообщения
        Optional<ChatMessage> messageOpt = chatMessageRepository.findById(messageId);

        if (messageOpt.isEmpty()) {
            throw new IllegalArgumentException("Message with ID " + messageId + " does not exist.");
        }

        ChatMessage message = messageOpt.get();
        message.setStatus(status);
        return chatMessageRepository.save(message);
    }

    @Override
    public void deleteMessageById(String id) {
        chatMessageRepository.deleteById(id);
    }

}
