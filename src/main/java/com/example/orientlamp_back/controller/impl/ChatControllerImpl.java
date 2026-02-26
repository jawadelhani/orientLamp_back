package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.ChatController;
import com.example.orientlamp_back.dto.ChatRequestDTO;
import com.example.orientlamp_back.dto.ChatResponseDTO;
import com.example.orientlamp_back.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatControllerImpl implements ChatController {

    private final ChatService chatService;

    @Override
    public ResponseEntity<ChatResponseDTO> chat(ChatRequestDTO request) {
        log.info("Chat request received, message length: {}", request.getMessage().length());
        ChatResponseDTO response = chatService.chat(request);
        return ResponseEntity.ok(response);
    }
}
