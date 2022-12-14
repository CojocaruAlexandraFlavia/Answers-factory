package com.example.answersfactory.model;

import com.example.answersfactory.enums.NotificationStatus;
import com.example.answersfactory.enums.NotificationType;
import org.jetbrains.annotations.NotNull;

public class NotificationMock {
    public static @NotNull Notification notification(){
        Notification notification = new Notification();
        notification.setId(1L);
        notification.setNotificationType(NotificationType.RECEIVED_ANSWER);
        notification.setNotificationStatus(NotificationStatus.UNSEEN);
        Question question = new Question();
        question.setId(1L);
        notification.setQuestion(question);
        return notification;
    }
}
