package com.example.answersfactory.model;

import com.example.answersfactory.model.dto.UserDto;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserDtoMock {

    public static @NotNull UserDto userDto() {
        UserDto dto = new UserDto();
        dto.setEmail("email@email.com");
        dto.setFirstName("firstName");
        dto.setLastName("lastName");
        dto.setAnswers(new ArrayList<>());
        dto.setQuestions(new ArrayList<>());
        dto.setSuggestions(new ArrayList<>());
        dto.setDescription("description");
        dto.setCorrectAnswers(5);
        return dto;
    }

}
