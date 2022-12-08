package com.example.answersfactory.model;

import com.example.answersfactory.model.dto.AnswerDto;
import org.jetbrains.annotations.NotNull;

public class AnswerDtoMock {

    public static @NotNull AnswerDto answerDto() {
        AnswerDto dto = new AnswerDto();
        dto.setQuestionId(1L);
        dto.setUserId(1L);
        return dto;
    }

}
