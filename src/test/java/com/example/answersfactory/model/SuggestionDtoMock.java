package com.example.answersfactory.model;

import com.example.answersfactory.model.dto.SuggestionDto;
import org.jetbrains.annotations.NotNull;

public class SuggestionDtoMock {
    public static @NotNull SuggestionDto suggestionDto(){
        SuggestionDto suggestionDto = new SuggestionDto();
        suggestionDto.setQuestionId(1L);
        suggestionDto.setUserId(2L);
        return suggestionDto;
    }
}
