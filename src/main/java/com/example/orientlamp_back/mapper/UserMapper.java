package com.example.orientlamp_back.mapper;

import com.example.orientlamp_back.dto.UserRequestDTO;
import com.example.orientlamp_back.dto.UserResponseDTO;
import com.example.orientlamp_back.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        return User.builder()
                .email(dto.getEmail())
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .userType(dto.getUserType())
                .age(dto.getAge())
                .currentStudyLevel(dto.getCurrentStudyLevel())
                .enabled(false)
                .build();
    }

    public UserResponseDTO toDTO(User entity) {
        if (entity == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .idUser(entity.getIdUser())
                .email(entity.getEmail())
                .enabled(entity.isEnabled())
                .username(entity.getUsername())
                .userType(entity.getUserType())
                .age(entity.getAge())
                .currentStudyLevel(entity.getCurrentStudyLevel())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public void updateEntityFromDTO(UserRequestDTO dto, User entity) {
        if (dto == null || entity == null) {
            return;
        }

        entity.setEmail(dto.getEmail());
        entity.setUsername(dto.getUsername());
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        entity.setUserType(dto.getUserType());
        entity.setAge(dto.getAge());
        entity.setCurrentStudyLevel(dto.getCurrentStudyLevel());
    }
}