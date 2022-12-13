package com.example.answersfactory.model;

import com.example.answersfactory.service.SuggestionService;
import org.jetbrains.annotations.NotNull;

import static com.example.answersfactory.model.QuestionMock.question;
import static com.example.answersfactory.model.UserMock.user;

public class SuggestionMock {
    public static @NotNull Suggestion suggestion(){
        Suggestion suggestion = new Suggestion();
        suggestion.setUser(user());
        suggestion.setQuestion(question());
        suggestion.setId(1L);
        return suggestion;
    }
}
