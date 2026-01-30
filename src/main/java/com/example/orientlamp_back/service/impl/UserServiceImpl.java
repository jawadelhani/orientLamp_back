package com.example.orientlamp_back.service.impl;

import com.example.orientlamp_back.dto.UserRequestDTO;
import com.example.orientlamp_back.dto.UserResponseDTO;
import com.example.orientlamp_back.entity.CurrentStudyLevel;
import com.example.orientlamp_back.entity.User;
import com.example.orientlamp_back.mapper.UserMapper;
import com.example.orientlamp_back.repository.UserRepository;
import com.example.orientlamp_back.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        log.info("Creating new user with email: {}", requestDTO.getEmail());

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("User with email " + requestDTO.getEmail() + " already exists");
        }
        User user = userMapper.toEntity(requestDTO);
        User savedUser = userRepository.save(user);

        log.info("User created successfully with ID: {}", savedUser.getIdUser());
        return userMapper.toDTO(savedUser);
    }

    @Override
    public UserResponseDTO updateUser(Long idUser, UserRequestDTO requestDTO) {
        log.info("Updating user with ID: {}", idUser);

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + idUser));

        if (!user.getEmail().equals(requestDTO.getEmail()) &&
                userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new RuntimeException("User with email " + requestDTO.getEmail() + " already exists");
        }
        userMapper.updateEntityFromDTO(requestDTO, user);
        User updatedUser = userRepository.save(user);

        log.info("User updated successfully with ID: {}", updatedUser.getIdUser());
        return userMapper.toDTO(updatedUser);
    }

    @Override
    public void deleteUser(Long idUser) {
        log.info("Deleting user with ID: {}", idUser);

        if (!userRepository.existsById(idUser)) {
            throw new RuntimeException("User not found with id: " + idUser);
        }

        userRepository.deleteById(idUser);
        log.info("User deleted successfully with ID: {}", idUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long idUser) {
        log.info("Fetching user with ID: {}", idUser);

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + idUser));

        return userMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        log.info("Fetching user with email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        return userMapper.toDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        log.info("Fetching all users");

        return userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByEnabled(boolean enabled) {
        log.info("Fetching users by enabled status: {}", enabled);

        return userRepository.findByEnabled(enabled).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByAgeBetween(Integer minAge, Integer maxAge) {
        log.info("Fetching users with age between {} and {}", minAge, maxAge);

        return userRepository.findByAgeBetween(minAge, maxAge).stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByCurrentStudyLevel(String currentStudyLevel) {
        log.info("Fetching users by current study level: {}", currentStudyLevel);
        // Delegate to repository using enum type
        CurrentStudyLevel level = CurrentStudyLevel.valueOf(currentStudyLevel);
        return userRepository.findByCurrentStudyLevel(level).stream()
            .map(userMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public void enableUser(Long idUser) {
        log.info("Enabling user with ID: {}", idUser);

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + idUser));

        user.setEnabled(true);
        userRepository.save(user);

        log.info("User enabled successfully with ID: {}", idUser);
    }

    @Override
    public void disableUser(Long idUser) {
        log.info("Disabling user with ID: {}", idUser);

        User user = userRepository.findById(idUser)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + idUser));

        user.setEnabled(false);
        userRepository.save(user);

        log.info("User disabled successfully with ID: {}", idUser);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}