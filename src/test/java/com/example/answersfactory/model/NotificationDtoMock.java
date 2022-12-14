package com.example.answersfactory.model;

import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.NotificationDto;
import org.jetbrains.annotations.NotNull;

public class NotificationDtoMock {
    public static @NotNull NotificationDto notificationDto() {
        NotificationDto dto = new NotificationDto();
        dto.setQuestionId(1L);
        return dto;
    }

}
