package com.example.answersfactory.model;

import org.jetbrains.annotations.NotNull;

public class NotificationMock {
    public static @NotNull Notification notification(){
        Notification notification = new Notification();
        notification.setId(1L);
        Question question = new Question();
        question.setId(1L);
        notification.setQuestion(question);
        return notification;
    }
}
