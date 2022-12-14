package com.example.answersfactory.model;

import com.example.answersfactory.enums.NotificationStatus;
import com.example.answersfactory.enums.NotificationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    private NotificationStatus notificationStatus;

    @ManyToOne
    private Question question;

    @PreRemove
    public void preRemove(){
        this.setQuestion(null);
    }

}
