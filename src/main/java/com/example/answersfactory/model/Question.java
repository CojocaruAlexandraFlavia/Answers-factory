package com.example.answersfactory.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String status;
    private String createDate;

    @OneToMany(mappedBy = "question", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "question", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Suggestion> suggestions;

    @OneToMany(mappedBy = "question", cascade= CascadeType.ALL)
    private List<Answer> answers;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne
    private Topic topic;

}
