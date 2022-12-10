package com.example.answersfactory.service;

import com.example.answersfactory.enums.TopicValue;
import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.Topic;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.repository.AnswerRepository;
import com.example.answersfactory.repository.BadgeRepository;
import com.example.answersfactory.repository.QuestionRepository;
import com.example.answersfactory.repository.TopicRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.example.answersfactory.model.AnswerDtoMock.answerDto;
import static com.example.answersfactory.model.AnswerMock.answer;
import static com.example.answersfactory.model.QuestionDtoMock.questionDto;
import static com.example.answersfactory.model.QuestionMock.question;
import static com.example.answersfactory.model.TopicMock.topic;
import static com.example.answersfactory.model.UserMock.user;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class QuestionServiceTest {
    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private UserService userService;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private QuestionService questionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void findQuestionById() {
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        Optional<Question> result = questionService.findQuestionById(1L);
        assertEquals(1, result.get().getId());
    }


//    @Test
//    void findAll(){
//
//        questionRepository.save(question());
//        List<Question> result = questionService.findAll();
//        assertTrue(result.contains(question()));
//
//    }

    @Test
    void saveQuestion(){

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto result = questionService.saveQuestion(questionDto());
        assertEquals(1L, result.getUserId());
    }

}
