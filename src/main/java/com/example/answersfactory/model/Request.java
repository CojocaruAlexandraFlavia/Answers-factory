package com.example.answersfactory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private String type;


    @ManyToOne
    @JsonIgnore
    private User sendUser;


    @ManyToOne
    @JsonIgnore
    private User receiveUser;
}
