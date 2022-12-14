package com.example.answersfactory.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String type;

    @ManyToOne
    private User sendUser;

    @ManyToOne
    private User receiveUser;
}
