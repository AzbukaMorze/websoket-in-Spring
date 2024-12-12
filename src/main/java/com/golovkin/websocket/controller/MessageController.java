package com.golovkin.websocket.controller;

import com.golovkin.websocket.model.ChatMessage;
import com.golovkin.websocket.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final ChatMessageService chatMessageService;

    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable String messageId) {
        chatMessageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<Optional<ChatMessage>> updateMessage(
            @PathVariable String messageId,
            @RequestBody String updatedContent) {
        Optional<ChatMessage> updatedMessage = chatMessageService.updateMessage(messageId, updatedContent);
        return ResponseEntity.ok(updatedMessage);
    }
}

