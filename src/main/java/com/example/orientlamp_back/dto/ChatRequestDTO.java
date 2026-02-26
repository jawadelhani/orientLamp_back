package com.example.orientlamp_back.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequestDTO {

    /** The latest user message */
    private String message;

    /** Previous conversation turns — ordered oldest first */
    private List<HistoryItem> history;

    /** AI provider: "groq" (default) or "gemini" */
    private String provider;

    /** Optional serialized user profile to personalise responses */
    private String userProfile;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HistoryItem {
        /** "user" or "model" */
        private String role;
        private String text;
    }
}