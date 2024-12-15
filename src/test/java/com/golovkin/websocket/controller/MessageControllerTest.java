package com.golovkin.websocket.controller;

import com.golovkin.websocket.model.ChatMessage;
import com.golovkin.websocket.service.ChatMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ChatMessageService chatMessageService;

    @InjectMocks
    private MessageController messageController;

    @BeforeEach
    void setUp() {
        // Настройка MockMvc
        mockMvc = MockMvcBuilders.standaloneSetup(messageController).build();
    }

    @Test
    void deleteMessage_Success() throws Exception {
        // Указываем, что метод deleteMessage должен пройти без ошибок
        doNothing().when(chatMessageService).deleteMessage(anyString());

        // Выполняем DELETE запрос и проверяем, что возвращается статус 204 (No Content)
        mockMvc.perform(delete("/messages/{messageId}", "message123"))
                .andExpect(status().isNoContent());

        // Проверяем, что deleteMessage был вызван один раз с правильным аргументом
        verify(chatMessageService, times(1)).deleteMessage("message123");
    }

}
