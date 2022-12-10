package com.example.answersfactory.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Question  implements Comparator<Question> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    private String status;
    private String createDate;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Notification> notifications;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Suggestion> suggestions;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @PreRemove
    public void preRemove() {
        this.answers.forEach(answer -> answer.setQuestion(null));
        this.suggestions.forEach(suggestion -> suggestion.setQuestion(null));
        this.notifications.forEach(notification -> notification.setQuestion(null));
    }

    @ManyToOne
    private User user;

    @ManyToOne
    private Topic topic;

    private Integer noOfAnswers(){
        return this.getAnswers().size();
    }

    @Override
    public int compare(Question o1, Question o2) {
        return o2.noOfAnswers() - o1.noOfAnswers();
    }
}
