package com.example.answersfactory.model;

import com.example.answersfactory.enums.QuestionStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

import static com.example.answersfactory.model.AnswerMock.answer;
import static com.example.answersfactory.model.TopicMock.topic;
import static com.example.answersfactory.model.UserMock.user;

public class QuestionMock {

    public static @NotNull Question question() {
        Question question = new Question();
        question.setId(1L);
        question.setStatus(QuestionStatus.OPEN);
        question.setUser(user());
        question.setTopic(topic());
        question.setAnswers(Collections.singletonList(answer()));
        return question;
    }

}
