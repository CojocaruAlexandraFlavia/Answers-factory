package com.example.answersfactory.controller;

import com.example.answersfactory.model.dto.SuggestionDto;
import com.example.answersfactory.service.SuggestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(SuggestionController.class)
class SuggestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private SuggestionService suggestionService;

    @BeforeEach
    void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    void testSaveSuggestion() {
        SuggestionDto dto = suggestionDto();
        when(suggestionService.saveSuggestion(any())).thenReturn(dto);
        mockMvc.perform(post("/suggestion/save")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1L));
    }

    @SneakyThrows
    @Test
    void testDeleteSuggestionById() {
        when(suggestionService.deleteSuggestion(anyLong())).thenReturn(true);
        mockMvc.perform(delete("/suggestion/delete-by-id/{id}", 1L))
                .andExpect(status().isOk());
    }

    private @NotNull SuggestionDto suggestionDto() {
        SuggestionDto dto = new SuggestionDto();
        dto.setQuestionId(1L);
        dto.setMessage("message");
        dto.setUserId(1L);
        return dto;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}