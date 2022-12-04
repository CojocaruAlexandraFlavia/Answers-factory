package com.example.answersfactory.repository;

import com.example.answersfactory.enums.TopicValue;
import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {

    Optional<Topic> findByName(TopicValue topicName);
}
