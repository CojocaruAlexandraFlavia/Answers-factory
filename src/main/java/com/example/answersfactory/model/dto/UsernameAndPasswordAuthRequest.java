package com.example.answersfactory.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameAndPasswordAuthRequest {

    private String email;
    private String password;
}
