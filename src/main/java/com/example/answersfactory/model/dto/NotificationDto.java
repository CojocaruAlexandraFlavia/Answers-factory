package com.example.answersfactory.model.dto;

import com.example.answersfactory.enums.NotificationStatus;
import lombok.Data;

@Data
public class NotificationDto {
    private String notificationType;
    private Long questionId;
    private String status;

    public NotificationDto(String notificationType, Long questionId, NotificationStatus status) {
        this.notificationType = notificationType;
        this.questionId = questionId;
        this.status = status.toString();
    }
}
