package com.example.answersfactory.service;

import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.RegisterUserRequest;
import com.example.answersfactory.model.dto.SuggestionDto;
import com.example.answersfactory.model.dto.UserDto;
import com.example.answersfactory.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static com.example.answersfactory.model.SuggestionDtoMock.suggestionDto;
import static com.example.answersfactory.model.UserMock.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findUserByEmail(){
        when(userRepository.findByEmail("email@email.com")).thenReturn(Optional.of(user()));
        Optional<User> result = userService.findUserByEmail("email@email.com");
        assertEquals("email@email.com", result.get().getEmail());
    }

    @Test
    void findUserById(){
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user()));
        Optional<User> result = userService.findUserById(1L);
        assertEquals(1L, result.get().getId());
    }

    @Test
    void insertUser(){
        User user = user();
        when(userRepository.save(any())).thenReturn(user);
        userService.insertUser(user);
        verify(userRepository).save(user);
    }

    @Test
    void registerUser(){

        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("email@gmail.com");
        request.setPassword("password");
        request.setFirstName("Ion");
        request.setLastName("Ion");
        when(passwordEncoder.encode(anyString())).thenReturn("password");
        when(userRepository.save(any())).thenReturn(user());
        UserDto user = userService.registerUser(request);

        assertEquals("firstName", user.getFirstName());



    }
}
