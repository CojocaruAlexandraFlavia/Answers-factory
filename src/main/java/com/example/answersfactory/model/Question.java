package com.example.answersfactory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String status;
    private String createDate;

    public Question() {
    }


    @OneToMany(mappedBy = "question", cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE })
    private List<Suggestion> suggestions;
//
//    @OneToMany(mappedBy = "question", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonIgnore
//    @Fetch(value = FetchMode.SUBSELECT)
//    private List<Suggestion> suggestions;

//    @OneToMany(mappedBy = "question", cascade= CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonIgnore
//    @Fetch(value = FetchMode.SUBSELECT)
//
//    private List<Answer> answers;

    @ManyToOne
    @JsonIgnore
    private User user;

    @ManyToOne
    @JsonIgnore
    private Topic topic;



}
