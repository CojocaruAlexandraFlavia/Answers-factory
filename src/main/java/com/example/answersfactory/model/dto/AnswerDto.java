package com.example.answersfactory.model.dto;

import com.example.answersfactory.model.Answer;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class AnswerDto {

    private String message;
    private Long questionId;
    private Long userId;
    private Long likes;
    private Long dislikes;
    private boolean acceptedStatus;
    private String date;

    public static @NotNull AnswerDto convertEntityToDto(@NotNull Answer answer){
        AnswerDto answerDto = new AnswerDto();
        answerDto.setDislikes(answer.getDislikes());
        answerDto.setLikes(answer.getLikes());
        answerDto.setMessage(answer.getMessage());
        answerDto.setAcceptedStatus(answer.isAcceptedStatus());
        answerDto.setDate(answer.getDate());
        answerDto.setUserId(answer.getUser().getId());
        answerDto.setQuestionId(answer.getQuestion().getId());
        return answerDto;
    }

}
