package com.golovkin.websocket.service;

import com.golovkin.websocket.exceptions.ChatRoomNotFoundException;
import com.golovkin.websocket.model.ChatMessage;
import com.golovkin.websocket.repository.ChatMessageRepository;
import com.golovkin.websocket.service.ChatMessageServiceImp;
import com.golovkin.websocket.service.ChatRoomServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Ensure Mockito annotations are processed
class ChatMessageServiceImpTest {

    @Mock
    private ChatMessageRepository chatMessageRepository;

    @Mock
    private ChatRoomServiceImpl chatRoomService;

    @InjectMocks
    private ChatMessageServiceImp chatMessageService;

    private ChatMessage chatMessage;

    @BeforeEach
    void setUp() {
        // Initialize the mocks
        chatMessage = new ChatMessage();
        chatMessage.setSenderId("sender123");
        chatMessage.setRecipientId("recipient123");
        chatMessage.setContent("Hello");
    }

    @Test
    void saveMessage_ChatRoomNotFound() {
        // Simulate the case where chat room is not found
        when(chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true))
                .thenReturn(Optional.empty());

        // Check that ChatRoomNotFoundException is thrown
        ChatRoomNotFoundException exception = assertThrows(
                ChatRoomNotFoundException.class,
                () -> chatMessageService.saveMessage(chatMessage)
        );

        assertEquals("Chat room not found for senderId sender123 and recipientId recipient123", exception.getMessage());
    }

    @Test
    void saveMessage_Success() {
        // Simulate that a chat room is found
        String chatId = "chat123";
        when(chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true))
                .thenReturn(Optional.of(chatId));

        // Call the saveMessage method
        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);

        // Verify that the message's chatId is set correctly
        assertEquals(chatId, savedMessage.getChatId());

        // Verify that the message was saved in the repository
        verify(chatMessageRepository, times(1)).save(chatMessage);
    }

    @Test
    void getChatMessages_NoChatFound() {
        // Simulate that the chat room doesn't exist
        when(chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), false))
                .thenReturn(Optional.empty());

        // Call the getChatMessages method
        var messages = chatMessageService.getChatMessages(chatMessage.getSenderId(), chatMessage.getRecipientId());

        // Verify that an empty list is returned
        assertTrue(messages.isEmpty());
    }

    @Test
    void getChatMessages_Success() {
        // Simulate that a chat room exists
        String chatId = "chat123";
        when(chatRoomService.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), false))
                .thenReturn(Optional.of(chatId));

        // Simulate that the repository returns a list of messages
        when(chatMessageRepository.findByChatId(chatId))
                .thenReturn(List.of(chatMessage));

        // Call the getChatMessages method
        var messages = chatMessageService.getChatMessages(chatMessage.getSenderId(), chatMessage.getRecipientId());

        // Verify that the list of messages is not empty and contains the correct message
        assertFalse(messages.isEmpty());
        assertEquals(1, messages.size());
        assertEquals(chatMessage, messages.getFirst());
    }

    @Test
    void updateMessage_MessageNotFound() {
        // Simulate that the message doesn't exist
        when(chatMessageRepository.findById("nonExistingMessageId"))
                .thenReturn(Optional.empty());

        // Call the updateMessage method and verify that an exception is thrown
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> chatMessageService.updateMessage("nonExistingMessageId", "Updated content")
        );

        assertEquals("Message not found", exception.getMessage());
    }


    @Test
    void deleteMessage_Success() {

        String messageId = "message123";
        ChatMessage existingMessage = new ChatMessage();
        existingMessage.setId(messageId);

        chatMessageService.deleteMessage(messageId);

        verify(chatMessageRepository, times(1)).deleteById(messageId);
    }
}
