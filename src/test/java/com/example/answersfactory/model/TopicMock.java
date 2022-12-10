package com.example.answersfactory.model;

import com.example.answersfactory.enums.TopicValue;
import org.jetbrains.annotations.NotNull;

public class TopicMock {

    public static @NotNull Topic topic() {
        Topic topic = new Topic();
        topic.setName(TopicValue.FOOD);
        return topic;
    }

}
