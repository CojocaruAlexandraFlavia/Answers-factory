package com.example.answersfactory.model;

import com.example.answersfactory.enums.QuestionStatus;
import com.example.answersfactory.enums.TopicValue;
import com.example.answersfactory.model.dto.QuestionDto;
import org.jetbrains.annotations.NotNull;

public class QuestionDtoMock {

    public static @NotNull QuestionDto questionDto() {
        QuestionDto dto = new QuestionDto();
        dto.setTopic("food");
        dto.setUserId(1L);
        dto.setStatus(QuestionStatus.OPEN.toString());
        return dto;
    }

}
