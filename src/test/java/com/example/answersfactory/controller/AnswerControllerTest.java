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

import java.util.ArrayList;
import java.util.Optional;

import static com.example.answersfactory.model.AnswerDtoMock.answerDto;
import static com.example.answersfactory.model.AnswerMock.answer;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
    void testSaveAnswer() {
        AnswerDto answerDto = answerDto();
        when(answerService.saveAnswer(any())).thenReturn(answerDto);
        mockMvc.perform(post("/answer/save")
                        .content(asJsonString(answerDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1L));
    }

    @SneakyThrows
    @Test
    void testFindAnswerById() {
        when(answerService.findAnswerById(anyLong())).thenReturn(Optional.of(answer()));
        mockMvc.perform(get("/answer/get-by-id/{id}", 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1L));
    }

    @SneakyThrows
    @Test
    void testDeleteAnswerByIdSuccess() {
        when(answerService.deleteAnswer(anyLong())).thenReturn(true);
        mockMvc.perform(delete("/answer/delete-by-id/{id}", 1L))
                .andExpect(status().isOk());
    }

    @SneakyThrows
    @Test
    void testDeleteAnswerByIdFailed() {
        when(answerService.deleteAnswer(anyLong())).thenReturn(false);
        mockMvc.perform(delete("/answer/delete-by-id/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @SneakyThrows
    @Test
    void testUpdateAnswer() {
        AnswerDto dto = answerDto();
        when(answerService.updateAnswer(anyLong(), any())).thenReturn(dto);
        mockMvc.perform(put("/answer/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1L));
    }

    @SneakyThrows
    @Test
    void testVoteResponse() {
        AnswerDto dto = answerDto();
        when(answerService.voteResponse(any())).thenReturn(dto);
        mockMvc.perform(put("/answer/vote-response")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(asJsonString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionId").value(1L));
    }

    @SneakyThrows
    @Test
    void testFilterResponsesByDateFails() {
        when(answerService.filterByDate(anyString())).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/answer/filter-by-date/{criteria}", "c")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @SneakyThrows
    @Test
    void testFilterResponsesByDateSuccess() {
        when(answerService.filterByDate(anyString())).thenReturn(singletonList(answerDto()));
        mockMvc.perform(get("/answer/filter-by-date/{criteria}", "week")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}