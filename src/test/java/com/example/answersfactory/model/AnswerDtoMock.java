package com.example.answersfactory.model;

import com.example.answersfactory.model.dto.AnswerDto;
import org.jetbrains.annotations.NotNull;

public class AnswerDtoMock {

    public static @NotNull AnswerDto answerDto() {
        AnswerDto dto = new AnswerDto();
        dto.setQuestionId(1L);
        dto.setUserId(1L);
        dto.setDate("22-12-2022 15:05:01");
        dto.setLikes(1L);
        dto.setDislikes(0L);
        dto.setMessage("message");
        return dto;
    }

}
