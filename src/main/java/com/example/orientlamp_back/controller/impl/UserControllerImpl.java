package com.example.orientlamp_back.controller.impl;

import com.example.orientlamp_back.controller.UserController;
import com.example.orientlamp_back.dto.UserRequestDTO;
import com.example.orientlamp_back.dto.UserResponseDTO;
import com.example.orientlamp_back.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserControllerImpl implements UserController {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponseDTO> createUser(UserRequestDTO requestDTO) {
        log.info("REST request to create User: {}", requestDTO.getEmail());
        UserResponseDTO responseDTO = userService.createUser(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public ResponseEntity<UserResponseDTO> updateUser(Long idUser, UserRequestDTO requestDTO) {
        log.info("REST request to update User with ID: {}", idUser);
        UserResponseDTO responseDTO = userService.updateUser(idUser, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<Void> deleteUser(Long idUser) {
        log.info("REST request to delete User with ID: {}", idUser);
        userService.deleteUser(idUser);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserById(Long idUser) {
        log.info("REST request to get User with ID: {}", idUser);
        UserResponseDTO responseDTO = userService.getUserById(idUser);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserByEmail(String email) {
        log.info("REST request to get User with email: {}", email);
        UserResponseDTO responseDTO = userService.getUserByEmail(email);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<UserResponseDTO> getUserByUsername(String username) {
        log.info("REST request to get User with username: {}", username);
        UserResponseDTO responseDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(responseDTO);
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        log.info("REST request to get all Users");
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getUsersByUserType(String userType) {
        log.info("REST request to get Users by type: {}", userType);
        List<UserResponseDTO> users = userService.getUsersByUserType(userType);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getUsersByEnabled(boolean enabled) {
        log.info("REST request to get Users by enabled status: {}", enabled);
        List<UserResponseDTO> users = userService.getUsersByEnabled(enabled);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getUsersByAgeBetween(Integer minAge, Integer maxAge) {
        log.info("REST request to get Users with age between {} and {}", minAge, maxAge);
        List<UserResponseDTO> users = userService.getUsersByAgeBetween(minAge, maxAge);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<List<UserResponseDTO>> getUsersByCurrentStudyLevel(String currentStudyLevel) {
        log.info("REST request to get Users by study level: {}", currentStudyLevel);
        List<UserResponseDTO> users = userService.getUsersByCurrentStudyLevel(currentStudyLevel);
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<Void> enableUser(Long idUser) {
        log.info("REST request to enable User with ID: {}", idUser);
        userService.enableUser(idUser);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> disableUser(Long idUser) {
        log.info("REST request to disable User with ID: {}", idUser);
        userService.disableUser(idUser);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Boolean> existsByEmail(String email) {
        log.info("REST request to check if User exists by email: {}", email);
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    @Override
    public ResponseEntity<Boolean> existsByUsername(String username) {
        log.info("REST request to check if User exists by username: {}", username);
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
}