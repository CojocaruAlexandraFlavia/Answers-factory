package com.example.answersfactory.model;


import com.example.answersfactory.enums.TopicValue;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TopicValue name;

    @OneToMany(mappedBy = "topic", cascade= CascadeType.ALL)
    private List<Question> questions;

}
