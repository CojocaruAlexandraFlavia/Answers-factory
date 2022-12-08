package com.example.answersfactory.model;

import org.jetbrains.annotations.NotNull;

public class UserMock {

    public static @NotNull User user() {
        User user = new User();
        user.setId(1L);
        return user;
    }

}
