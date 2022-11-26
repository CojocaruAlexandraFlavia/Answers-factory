package com.example.answersfactory.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "badges", cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<User> users;

}
