package com.golovkin.websocket.service;

import com.golovkin.websocket.model.ChatRoom;
import com.golovkin.websocket.repository.ChatRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ChatRoomServiceImplTest {

    @Mock
    private ChatRoomRepository chatRoomRepository;  // Mock the ChatRoomRepository

    @InjectMocks
    private ChatRoomServiceImpl chatRoomService;   // Inject the mocks into the service

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize the mocks
    }

    @Test
    void getChatRoomId_ChatRoomExists() {
        // Given
        String senderId = "sender1";
        String recipientId = "recipient1";
        String chatId = "sender1_recipient1";
        ChatRoom chatRoom = new ChatRoom(chatId, senderId, recipientId);


        // When
        when(chatRoomRepository.findBySenderIdOrRecipientId(senderId, recipientId)).thenReturn(java.util.Optional.of(chatRoom));

        // Then
        assertEquals(chatId, chatRoomService.getChatRoomId(senderId, recipientId, false).orElseThrow());
    }

    @Test
    void getChatRoomId_ChatRoomNotFoundAndNoCreate() {
        // Given
        String senderId = "sender1";
        String recipientId = "recipient1";

        // When
        when(chatRoomRepository.findBySenderIdOrRecipientId(senderId, recipientId)).thenReturn(java.util.Optional.empty());

        // Then
        assertTrue(chatRoomService.getChatRoomId(senderId, recipientId, false).isEmpty());
    }

    @Test
    void getChatRoomId_ChatRoomNotFoundAndCreateNew() {
        // Given
        String senderId = "sender1";
        String recipientId = "recipient1";

        // When
        when(chatRoomRepository.findBySenderIdOrRecipientId(senderId, recipientId)).thenReturn(java.util.Optional.empty());
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(new ChatRoom("sender1_recipient1", senderId, recipientId));

        // Then
        assertEquals("sender1_recipient1", chatRoomService.getChatRoomId(senderId, recipientId, true).orElseThrow());
    }

    @Test
    void createChatId() {
        // Given
        String senderId = "sender1";
        String recipientId = "recipient1";
        String chatId = "sender1_recipient1";

        // When
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(new ChatRoom(chatId, senderId, recipientId));

        // Then
        assertEquals(chatId, chatRoomService.createChatId(senderId, recipientId));
        verify(chatRoomRepository, times(2)).save(any(ChatRoom.class));  // Ensure that save is called twice
    }
}
