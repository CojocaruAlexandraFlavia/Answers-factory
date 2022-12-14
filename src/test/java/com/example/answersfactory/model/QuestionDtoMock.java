package com.example.answersfactory.model;

import com.example.answersfactory.enums.QuestionStatus;
import com.example.answersfactory.model.dto.QuestionDto;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.example.answersfactory.model.AnswerDtoMock.answerDto;
import static java.util.Collections.singletonList;

public class QuestionDtoMock {

    public static @NotNull QuestionDto questionDto() {
        QuestionDto dto = new QuestionDto();
        dto.setTopic("food");
        dto.setUserId(1L);
        dto.setMessage("message");
        dto.setCreateDate("22-12-2022 13:09:01");
        dto.setAnswers(singletonList(answerDto()));
        dto.setNotifications(new ArrayList<>());
        dto.setSuggestions(new ArrayList<>());
        dto.setStatus(QuestionStatus.OPEN.toString());
        return dto;
    }

}
