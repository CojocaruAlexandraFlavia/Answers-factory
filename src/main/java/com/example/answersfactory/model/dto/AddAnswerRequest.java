package com.example.answersfactory.model.dto;

import lombok.Data;

@Data
public class AddAnswerRequest {
    private Long questionId;
    private Long userId;
    private String message;
}
