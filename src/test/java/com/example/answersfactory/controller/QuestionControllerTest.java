package com.example.answersfactory.controller;

import com.example.answersfactory.enums.NotificationStatus;
import com.example.answersfactory.model.dto.CloseQuestion;
import com.example.answersfactory.model.dto.MarkAnswerRequest;
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

import java.util.ArrayList;
import java.util.Optional;

import static com.example.answersfactory.model.QuestionDtoMock.questionDto;
import static com.example.answersfactory.model.QuestionMock.question;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
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
                .andExpect(jsonPath("$.topic").value("food"));
    }

    @SneakyThrows
    @Test
    void testFindQuestionByIdOK() {
        when(questionService.findQuestionByIdDto(anyLong())).thenReturn(questionDto());
        mockMvc.perform(get("/question/get-by-id/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @SneakyThrows
    @Test
    void testFindQuestionByIdNotOK() {
        when(questionService.findQuestionByIdDto(anyLong())).thenReturn(null);
        mockMvc.perform(get("/question/get-by-id/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testDeleteQuestionByIdOK() {
        when(questionService.deleteQuestion(anyLong())).thenReturn(true);
        mockMvc.perform(delete("/question/delete-by-id/{id}", 1L))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testDeleteQuestionByIdNotOK() {
        when(questionService.deleteQuestion(anyLong())).thenReturn(false);
        mockMvc.perform(delete("/question/delete-by-id/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testUpdateQuestionOK() {
        QuestionDto dto = questionDto();
        when(questionService.updateQuestion(anyLong(), any())).thenReturn(dto);
        mockMvc.perform(put("/question/update/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @SneakyThrows
    @Test
    void testUpdateQuestionNotOK() {
        when(questionService.updateQuestion(anyLong(), any())).thenReturn(null);
        mockMvc.perform(put("/question/update/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(questionDto())))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testSeeNotificationOK() {
        NotificationDto dto = new NotificationDto("type", 1L, NotificationStatus.UNSEEN);
        when(questionService.seeNotification(anyLong())).thenReturn(dto);
        mockMvc.perform(patch("/question/see-notification/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1L));
    }

    @SneakyThrows
    @Test
    void testSeeNotificationNotOK() {
        when(questionService.seeNotification(anyLong())).thenReturn(null);
        mockMvc.perform(patch("/question/see-notification/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void testFindAllByPopularityEmpty() {
        when(questionService.findAll()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/question/get-by-popularity")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @SneakyThrows
    void testFindAllByPopularityNotEmpty() {
        when(questionService.findAll()).thenReturn(singletonList(question()));
        mockMvc.perform(get("/question/get-by-popularity")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @SneakyThrows
    @Test
    void testOrderAnswersNotOK() {
        when(questionService.sortByOption(anyLong(), anyString(), anyString())).thenReturn(null);
        mockMvc.perform(get("/question/{id}/answers-order-by/{option}/{type}", 1L, "date", "asc")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testOrderAnswersOK() {
        when(questionService.sortByOption(anyLong(), anyString(), anyString())).thenReturn(questionDto());
        mockMvc.perform(get("/question/{id}/answers-order-by/{option}/{type}", 1L, "date", "asc")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answers", hasSize(1)));
    }

    @Test
    @SneakyThrows
    void testMarkAcceptedAnswerOK() {
        MarkAnswerRequest request = new MarkAnswerRequest();
        request.setAnswerId(1L);
        request.setQuestionId(1L);
        request.setUserId(1L);
        when(questionService.markAcceptedAnswer(anyLong(), anyLong(), anyLong())).thenReturn(questionDto());
        mockMvc.perform(put("/question/mark-accepted-answer")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("food"));
    }

    @Test
    @SneakyThrows
    void testMarkAcceptedAnswerNotOK() {
        when(questionService.markAcceptedAnswer(anyLong(), anyLong(), anyLong())).thenReturn(null);
        mockMvc.perform(put("/question/mark-accepted-answer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(asJsonString(new MarkAnswerRequest())))
                .andExpect(status().isNotFound());
    }

    @Test
    @SneakyThrows
    void testCloseQuestionOK() {
        CloseQuestion request = new CloseQuestion();
        request.setQuestionId(1L);
        request.setUserId(1L);
        when(questionService.closeQuestion(anyLong(), anyLong())).thenReturn(questionDto());
        mockMvc.perform(put("/question/close-question")
                .content(asJsonString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.topic").value("food"));
    }

    @Test
    @SneakyThrows
    void testCloseQuestionNotOK() {
        when(questionService.closeQuestion(anyLong(), anyLong())).thenReturn(null);
        mockMvc.perform(put("/question/close-question")
                        .content(asJsonString(new CloseQuestion()))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}