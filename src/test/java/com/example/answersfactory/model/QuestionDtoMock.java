package com.example.answersfactory.model;

import com.example.answersfactory.model.dto.QuestionDto;
import org.jetbrains.annotations.NotNull;

public class QuestionDtoMock {

    public static @NotNull QuestionDto questionDto() {
        QuestionDto dto = new QuestionDto();
        dto.setTopic("topic");
        dto.setUserId(1L);
        dto.setStatus("status");
        return dto;
    }

}
