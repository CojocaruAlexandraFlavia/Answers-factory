package com.example.answersfactory.model.dto;

import com.example.answersfactory.enums.QuestionStatus;
import com.example.answersfactory.model.*;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Data
public class QuestionDto {

    private String message;
    private String createDate;
    private String status;
    private Long userId;
    private String topic;
    private List<NotificationDto> notifications;
    private List<SuggestionDto> suggestions;
    private List<AnswerDto> answers;

    public static @NotNull QuestionDto convertEntityToDto(@NotNull Question question){
        QuestionDto dto = new QuestionDto();
        dto.setStatus(question.getStatus().toString());
        dto.setMessage(question.getMessage());
        dto.setCreateDate(question.getCreateDate());
        dto.setUserId(question.getUser().getId());
        dto.setTopic(question.getTopic().getName().toString());

        if(question.getNotifications() != null){
            dto.setNotifications(question.getNotifications().stream().map(notification -> new NotificationDto(notification.getNotificationType().toString(),
                    notification.getQuestion().getId(), notification.getNotificationStatus())).collect(Collectors.toList()));
        }
        if(question.getAnswers() !=null){
            dto.setAnswers(question.getAnswers().stream().map(AnswerDto::convertEntityToDto).collect(toList()));
        }

        if(question.getSuggestions() != null){
            dto.setSuggestions(question.getSuggestions().stream().map(SuggestionDto::convertEntityToDto).collect(toList()));
        }
         return dto;
    }

}
