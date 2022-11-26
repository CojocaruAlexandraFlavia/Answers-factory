package com.example.answersfactory.model.dto;

import com.example.answersfactory.model.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Data
public class QuestionDto {

    private String message;
    private String createDate;
    private String status;
    private Long userId;
    private Topic topic;
    private List<Notification> notifications;
    private List<Suggestion> suggestions;
    private List<Answer> answers;

    public static @NotNull QuestionDto convertEntityToDto(@NotNull Question question){
        QuestionDto dto = new QuestionDto();
        dto.setStatus(question.getStatus());
        dto.setMessage(question.getMessage());
        dto.setCreateDate(question.getCreateDate());
        dto.setUserId(question.getUser().getId());
        dto.setTopic(question.getTopic());
        dto.setNotifications(question.getNotifications());
        dto.setSuggestions(question.getSuggestions());
        dto.setAnswers(question.getAnswers());
        return dto;
    }

}
