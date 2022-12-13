package com.example.answersfactory.model;

import com.example.answersfactory.enums.BadgeType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BadgeType name;

    @ManyToMany(mappedBy = "badges", cascade= CascadeType.ALL)
    private Set<User> users;

}
