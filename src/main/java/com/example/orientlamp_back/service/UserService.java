package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.UserRequestDTO;
import com.example.orientlamp_back.dto.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO updateUser(Long idUser, UserRequestDTO requestDTO);

    void deleteUser(Long idUser);

    UserResponseDTO getUserById(Long idUser);

    UserResponseDTO getUserByEmail(String email);

    UserResponseDTO getUserByUsername(String username);

    List<UserResponseDTO> getAllUsers();

    List<UserResponseDTO> getUsersByUserType(String userType);

    List<UserResponseDTO> getUsersByEnabled(boolean enabled);

    List<UserResponseDTO> getUsersByAgeBetween(Integer minAge, Integer maxAge);

    List<UserResponseDTO> getUsersByCurrentStudyLevel(String currentStudyLevel);

    void enableUser(Long idUser);

    void disableUser(Long idUser);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);
}