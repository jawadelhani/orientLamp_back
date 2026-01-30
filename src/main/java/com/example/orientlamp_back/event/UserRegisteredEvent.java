package com.example.orientlamp_back.event;

import com.example.orientlamp_back.entity.User;

public record UserRegisteredEvent(User user, String token, String baseUrl) {}
