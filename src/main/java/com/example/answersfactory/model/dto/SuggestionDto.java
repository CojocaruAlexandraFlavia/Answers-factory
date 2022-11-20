package com.example.answersfactory.model.dto;

import com.example.answersfactory.model.Suggestion;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class SuggestionDto {

    private String message;
    private Long questionId;
    private Long userId;

    public static @NotNull SuggestionDto convertEntityToDto(@NotNull Suggestion suggestion){
        SuggestionDto suggestionDto = new SuggestionDto();
        suggestionDto.setMessage(suggestion.getMessage());
        suggestionDto.setUserId(suggestion.getUser().getId());
        suggestionDto.setQuestionId(suggestion.getQuestion().getId());
        return suggestionDto;
    }

}
