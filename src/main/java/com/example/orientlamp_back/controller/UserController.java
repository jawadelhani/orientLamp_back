package com.example.orientlamp_back.controller;

import com.example.orientlamp_back.dto.UserRequestDTO;
import com.example.orientlamp_back.dto.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User", description = "User management APIs")
@RequestMapping("/api/users")
public interface UserController {

    @Operation(summary = "Create a new user")
    @PostMapping
    ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO requestDTO);

    @Operation(summary = "Update an existing user")
    @PutMapping("/{idUser}")
    ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long idUser,
            @Valid @RequestBody UserRequestDTO requestDTO);

    @Operation(summary = "Delete a user")
    @DeleteMapping("/{idUser}")
    ResponseEntity<Void> deleteUser(@PathVariable Long idUser);

    @Operation(summary = "Get user by ID")
    @GetMapping("/{idUser}")
    ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long idUser);

    @Operation(summary = "Get user by email")
    @GetMapping("/email/{email}")
    ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email);



    @Operation(summary = "Get all users")
    @GetMapping
    ResponseEntity<List<UserResponseDTO>> getAllUsers();


    @Operation(summary = "Get users by enabled status")
    @GetMapping("/enabled/{enabled}")
    ResponseEntity<List<UserResponseDTO>> getUsersByEnabled(@PathVariable boolean enabled);

    @Operation(summary = "Get users by age range")
    @GetMapping("/age")
    ResponseEntity<List<UserResponseDTO>> getUsersByAgeBetween(
            @RequestParam Integer minAge,
            @RequestParam Integer maxAge);

    @Operation(summary = "Get users by current study level")
    @GetMapping("/study-level/{currentStudyLevel}")
        ResponseEntity<List<UserResponseDTO>> getUsersByCurrentStudyLevel(@PathVariable String currentStudyLevel);

    @Operation(summary = "Enable a user")
    @PatchMapping("/{idUser}/enable")
    ResponseEntity<Void> enableUser(@PathVariable Long idUser);

    @Operation(summary = "Disable a user")
    @PatchMapping("/{idUser}/disable")
    ResponseEntity<Void> disableUser(@PathVariable Long idUser);

    @Operation(summary = "Check if user exists by email")
    @GetMapping("/exists/email/{email}")
    ResponseEntity<Boolean> existsByEmail(@PathVariable String email);


}