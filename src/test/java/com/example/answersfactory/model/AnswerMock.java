package com.example.answersfactory.model;

import org.jetbrains.annotations.NotNull;

import static com.example.answersfactory.model.UserMock.user;

public class AnswerMock {

    public static @NotNull Answer answer() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setDate("10-10-2022 12:12:12");
        Question question = new Question();
        question.setId(1L);
        answer.setQuestion(question);
        answer.setUser(user());
        return answer;
    }

}
