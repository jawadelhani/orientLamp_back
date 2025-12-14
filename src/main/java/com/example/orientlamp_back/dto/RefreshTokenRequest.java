package com.example.orientlamp_back.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Refresh Token Request
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    private String refreshToken;
}
