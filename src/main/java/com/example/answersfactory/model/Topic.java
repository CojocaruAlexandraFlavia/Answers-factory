package com.example.answersfactory.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Topic() {
    }

    @OneToMany(mappedBy = "topic", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

}
