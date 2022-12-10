package com.example.answersfactory.controller;

import com.example.answersfactory.enums.NotificationStatus;
import com.example.answersfactory.model.dto.NotificationDto;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.service.QuestionService;
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

import java.util.Optional;

import static com.example.answersfactory.model.QuestionDtoMock.questionDto;
import static com.example.answersfactory.model.QuestionMock.question;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @SneakyThrows
    @Test
    void testSaveQuestions() {
        QuestionDto dto = questionDto();
        when(questionService.saveQuestion(any())).thenReturn(dto);
        mockMvc.perform(post("/question/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("topic"));
    }

    @SneakyThrows
    @Test
    void testFindQuestionById() {
        when(questionService.findQuestionById(anyLong())).thenReturn(Optional.of(question()));
        mockMvc.perform(get("/question/get-by-id/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("status"));
    }

    @SneakyThrows
    @Test
    void testDeleteQuestionById() {
        when(questionService.deleteQuestion(anyLong())).thenReturn(true);
        mockMvc.perform(delete("/question/delete-by-id/{id}", 1L))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testUpdateQuestion() {
        QuestionDto dto = questionDto();
        when(questionService.updateQuestion(anyLong(), any())).thenReturn(dto);
        mockMvc.perform(put("/question/update/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("status"));
    }

    @Test
    void addAnswer() {
    }

    @SneakyThrows
    @Test
    void testSeeNotification() {
        NotificationDto dto = new NotificationDto("type", 1L, NotificationStatus.UNSEEN);
        when(questionService.seeNotification(anyLong())).thenReturn(dto);
        mockMvc.perform(patch("/question/see-notification/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1L));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}