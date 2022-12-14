package com.example.answersfactory.model.dto;

import lombok.Data;

@Data
public class VoteResponseRequest {

    private Long responseId;
    private Long userId;
    private int option;

}
