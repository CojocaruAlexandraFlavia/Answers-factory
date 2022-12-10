package com.example.answersfactory.model;

import org.jetbrains.annotations.NotNull;

public class UserMock {

    public static @NotNull User user() {
        User user = new User();
        user.setId(1L);
        user.setPassword("pass");
        user.setEmail("email@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        return user;
    }

}