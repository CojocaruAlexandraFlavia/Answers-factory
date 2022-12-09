package com.example.answersfactory.model.dto;

import com.example.answersfactory.model.User;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Data
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private int correctAnswers;
    private String description;
    private List<AnswerDto> answers;
    private List<QuestionDto> questions;
    private List<SuggestionDto> suggestions;

    public static @NotNull UserDto convertEntityToDto(@NotNull User user){
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setDescription(user.getDescription());
        dto.setCorrectAnswers(user.getCorrectAnswers());
        if (user.getAnswers() != null) {
            dto.setAnswers(user.getAnswers().stream().map(AnswerDto::convertEntityToDto).collect(toList()));
        }
        if (user.getQuestions() != null) {
            dto.setQuestions(user.getQuestions().stream().map(QuestionDto::convertEntityToDto).collect(toList()));
        }
        if(user.getSuggestions() != null) {
            dto.setSuggestions(user.getSuggestions().stream().map(SuggestionDto::convertEntityToDto).collect(toList()));
        }
        return dto;
    }

}
