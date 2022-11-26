package com.example.answersfactory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private long likes;
    private long dislikes;
    private boolean acceptedStatus;
    private String date;

    @ManyToOne
    @JsonIgnore
    private Question question;

    @ManyToOne
    @JsonIgnore
    private User user;

}
