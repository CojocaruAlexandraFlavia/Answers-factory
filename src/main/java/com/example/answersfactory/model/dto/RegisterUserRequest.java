package com.example.answersfactory.model.dto;

import lombok.Data;

@Data
public class RegisterUserRequest {

    private String email;
    private String password;
    private String description;
    private String firstName;
    private String lastName;
}
