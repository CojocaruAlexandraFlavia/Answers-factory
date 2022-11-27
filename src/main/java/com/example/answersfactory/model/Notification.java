package com.example.answersfactory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String notificationType;

    @ManyToOne
    private Question question;

    @PreRemove
    public void preRemove(){
        this.setQuestion(null);
    }

}
