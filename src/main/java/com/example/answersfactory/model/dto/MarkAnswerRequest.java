package com.example.answersfactory.model.dto;

import lombok.Data;

@Data
public class MarkAnswerRequest {
    private Long questionId;
    private Long userId;
    private Long answerId;
}
