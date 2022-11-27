package com.example.answersfactory.model.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private String notificationType;
    private Long questionId;

    public NotificationDto(String notificationType, Long questionId) {
        this.notificationType = notificationType;
        this.questionId = questionId;
    }
}
