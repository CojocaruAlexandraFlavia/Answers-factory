package com.example.answersfactory.model;

import com.example.answersfactory.enums.Role;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;

public class UserMock {

    public static @NotNull User user() {
        User user = new User();
        user.setId(1L);
        user.setPassword("pass");
        user.setEmail("email@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setRole(Role.ROLE_USER);
        user.setBadges(new HashSet<>());
        user.setAnswers(new ArrayList<>());
        user.setVotedAnswers(new HashSet<>());
        user.setBanningRaisedRequests(new ArrayList<>());
        user.setBanningReceivedRequests(new ArrayList<>());
        return user;
    }

}
