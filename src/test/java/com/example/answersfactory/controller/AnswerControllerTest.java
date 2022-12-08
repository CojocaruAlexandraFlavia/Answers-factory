package com.example.answersfactory.controller;

import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.service.AnswerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(AnswerController.class)
class AnswerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AnswerService answerService;

    @BeforeEach
    void setUp(){
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    void saveAnswer() {
        AnswerDto answerDto = answerDto();
        when(answerService.saveAnswer(any())).thenReturn(answerDto);
        mockMvc.perform(post("/answer/save")
                        .content(asJsonString(answerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1L));
    }

    @Test
    void findAnswerById() {
    }

    @Test
    void deleteAnswerById() {
    }

    @Test
    void updateAnswer() {
    }

    @Test
    void voteResponse() {
    }

    @Test
    void filterResponsesByDate() {
    }

    private static AnswerDto answerDto(){
        AnswerDto dto = new AnswerDto();
        dto.setQuestionId(1L);
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