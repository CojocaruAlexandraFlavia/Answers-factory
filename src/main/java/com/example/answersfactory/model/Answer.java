package com.example.answersfactory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
    private Question question;

    @ManyToOne
    private User user;

    @PreRemove
    public void preRemove(){
        this.setQuestion(null);
    }

}
