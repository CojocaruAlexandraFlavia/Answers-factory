package com.example.answersfactory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private User sendUser;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiveUser;
}
