package com.example.answersfactory.controller;

import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.UserDto;
import com.example.answersfactory.model.dto.UsernameAndPasswordAuthRequest;
import com.example.answersfactory.service.UserService;
import com.example.answersfactory.util.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static com.example.answersfactory.model.UserDtoMock.userDto;
import static com.example.answersfactory.model.UserMock.user;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    void testLoginFails() {
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(asJsonString(usernameAndPasswordAuthRequest())))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testLoginSuccess() {
        User user = user();
        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any()))
                .thenReturn(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
        when(jwtUtils.generateJwtToken(any())).thenReturn("token");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(usernameAndPasswordAuthRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token"));
    }

    @SneakyThrows
    @Test
    void testRegister() {
        UserDto dto = userDto();
        when(userService.registerUser(any())).thenReturn(dto);
        mockMvc.perform(post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("firstName"));
    }

    private static @NotNull UsernameAndPasswordAuthRequest usernameAndPasswordAuthRequest() {
        UsernameAndPasswordAuthRequest request = new UsernameAndPasswordAuthRequest();
        request.setPassword("pass");
        request.setEmail("email");
        return request;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}