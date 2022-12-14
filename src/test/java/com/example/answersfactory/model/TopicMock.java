package com.example.answersfactory.model;

import com.example.answersfactory.enums.TopicValue;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TopicMock {

    public static @NotNull Topic topic() {
        Topic topic = new Topic();
        topic.setName(TopicValue.FOOD);
        topic.setId(1L);
        topic.setQuestions(new ArrayList<>());
        return topic;
    }

}
