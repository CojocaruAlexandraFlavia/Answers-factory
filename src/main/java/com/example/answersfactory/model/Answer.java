package com.example.answersfactory.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.Comparator;

@Entity
@Getter
@Setter
public class Answer implements Comparator<Answer>, Comparable<Answer> {

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

    @Override
    public int compareTo(@NotNull Answer o) {
        return(this.date).compareTo(o.date);
    }

    @Override
    public int compare(Answer o1, Answer o2) {
        return (int) (o1.likes - o2.likes);
    }


}
