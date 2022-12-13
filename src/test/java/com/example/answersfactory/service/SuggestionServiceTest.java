package com.example.answersfactory.service;

import com.example.answersfactory.model.Suggestion;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class SuggestionServiceTest {

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
    void saveSuggestion(){
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(questionService.findQuestionById(anyLong())).thenReturn(Optional.of(question()));
        when(suggestionRepository.save(any())).thenReturn(suggestion());
        SuggestionDto suggestionDto = suggestionService.saveSuggestion(suggestionDto());
        assertEquals(2L, suggestionDto.getUserId());
    }
    @Test
    void delete(){

    }


}






