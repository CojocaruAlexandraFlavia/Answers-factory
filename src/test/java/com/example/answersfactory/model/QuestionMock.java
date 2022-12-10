package com.example.answersfactory.model;

import org.jetbrains.annotations.NotNull;

import static com.example.answersfactory.model.TopicMock.topic;
import static com.example.answersfactory.model.UserMock.user;

public class QuestionMock {

    public static @NotNull Question question() {
        Question question = new Question();
        question.setId(1L);
        question.setStatus("status");
        question.setUser(user());
        question.setTopic(topic());
        return question;
    }

}
