package com.example.answersfactory.model;

import com.example.answersfactory.enums.QuestionStatus;
import org.jetbrains.annotations.NotNull;

import static com.example.answersfactory.model.UserMock.user;

public class AnswerMock {

    public static @NotNull Answer answer() {
        Answer answer = new Answer();
        answer.setId(1L);
        answer.setDate("10-10-2022 12:12:12");
        Question question = new Question();
        question.setId(1L);
        question.setStatus(QuestionStatus.OPEN);
        answer.setQuestion(question);
        answer.setUser(user());
        answer.setLikes(100);
        answer.setDislikes(12);
        answer.setAcceptedStatus(true);
        return answer;
    }

}
