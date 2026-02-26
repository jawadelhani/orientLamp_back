package com.example.orientlamp_back.service;

import com.example.orientlamp_back.dto.UserBasicUpdateDTO;
import com.example.orientlamp_back.dto.UserRequestDTO;
import com.example.orientlamp_back.dto.UserResponseDTO;
import com.example.orientlamp_back.entity.CurrentStudyLevel;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO updateUser(Long idUser, UserRequestDTO requestDTO);

    UserResponseDTO updateUserBasic(Long idUser, UserBasicUpdateDTO dto);

    void deleteUser(Long idUser);

    UserResponseDTO getUserById(Long idUser);

    UserResponseDTO getUserByEmail(String email);


    List<UserResponseDTO> getAllUsers();


    List<UserResponseDTO> getUsersByEnabled(boolean enabled);

    List<UserResponseDTO> getUsersByAgeBetween(Integer minAge, Integer maxAge);

    List<UserResponseDTO> getUsersByCurrentStudyLevel(String currentStudyLevel);

    void enableUser(Long idUser);

    void disableUser(Long idUser);

    boolean existsByEmail(String email);

}