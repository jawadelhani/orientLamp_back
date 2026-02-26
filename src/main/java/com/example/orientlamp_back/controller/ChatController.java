package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.ChatRequestDTO;
import com.example.orientlamp_back.dto.ChatResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chat", description = "AI orientation chatbot")
@RequestMapping("/api/chat")
public interface ChatController {

    @Operation(summary = "Send a message to OrientIA")
    @PostMapping
    ResponseEntity<ChatResponseDTO> chat(@Valid @RequestBody ChatRequestDTO request);
}
