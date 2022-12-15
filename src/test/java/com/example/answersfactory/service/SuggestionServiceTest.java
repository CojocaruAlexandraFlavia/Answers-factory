package com.example.answersfactory.service;

import com.example.answersfactory.model.Suggestion;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.SuggestionDto;
import com.example.answersfactory.repository.SuggestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.example.answersfactory.model.QuestionMock.question;
import static com.example.answersfactory.model.SuggestionDtoMock.suggestionDto;
import static com.example.answersfactory.model.SuggestionMock.suggestion;
import static com.example.answersfactory.model.UserMock.user;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class SuggestionServiceTest {

    @Mock
    private SuggestionRepository suggestionRepository;

    @Mock
    private UserService userService;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private SuggestionService suggestionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findSuggestionById(){
        when(suggestionRepository.findById(anyLong())).thenReturn(Optional.of(suggestion()));
        Optional<Suggestion> result = suggestionService.findSuggestionById(1L);
        assertEquals(1, result.get().getId());
    }

    @Test
    void saveSuggestionOK(){
        User user = user();
        user.setId(2L);
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user));
        when(questionService.findQuestionById(anyLong())).thenReturn(Optional.of(question()));
        when(suggestionRepository.save(any())).thenReturn(suggestion());
        SuggestionDto suggestionDto = suggestionService.saveSuggestion(suggestionDto());
        assertNotNull(suggestionDto);
    }

    @Test
    void saveSuggestionNotOk() {
        when(userService.findUserById(anyLong())).thenReturn(Optional.empty());
        when(questionService.findQuestionById(anyLong())).thenReturn(Optional.empty());
        assertNull(suggestionService.saveSuggestion(suggestionDto()));
    }
    @Test
    void deleteTrue(){
        when(suggestionRepository.findById(anyLong())).thenReturn(Optional.of(suggestion()));
        assertTrue(suggestionService.deleteSuggestion(1L));
    }

    @Test
    void deleteFalse(){
        when(suggestionRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertFalse(suggestionService.deleteSuggestion(1L));
    }

}






