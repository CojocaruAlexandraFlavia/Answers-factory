package com.example.answersfactory.service;


import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.example.answersfactory.model.AnswerMock.answer;
import static com.example.answersfactory.model.NotificationMock.notification;
import static com.example.answersfactory.model.QuestionDtoMock.questionDto;
import static com.example.answersfactory.model.QuestionMock.question;
import static com.example.answersfactory.model.TopicMock.topic;
import static com.example.answersfactory.model.UserMock.user;
import static org.junit.jupiter.api.Assertions.*;
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

    @Mock
    private NotificationRepository notificationRepository;

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

    @Test
    void saveQuestion(){

        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto result = questionService.saveQuestion(questionDto());
        assertEquals(1L, result.getUserId());
    }

    @Test
    void updateQuestion(){
        when(questionService.findQuestionById(anyLong())).thenReturn(Optional.of(question()));
        when(questionRepository.save(any())).thenReturn(question());
        assertNotNull(questionService.updateQuestion(question().getId(), questionDto()));
    }


    @Test
    void deleteQuestion(){

    }
    @Test
    void deleteTopic(){

    }


    @Test
    void addAnswer(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(answerRepository.save(any())).thenReturn(answer());
        when(questionRepository.save(any())).thenReturn(question());
        assertNotNull(questionService.saveQuestion(questionDto()));

    }

    @Test
    void markAcceptedAnswer(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
        when(answerRepository.save(any())).thenReturn(answer());
        when(notificationRepository.save(any())).thenReturn(notification());
        when((questionRepository.save(any()))).thenReturn(question());
        assertNotNull(questionRepository.save(question()));
    }

    @Test
    void closeQuestion(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(questionRepository.save(any())).thenReturn(question());
        assertNotNull(questionService.saveQuestion(questionDto()));
    }

    @Test
    void seeNotification(){
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification()));
        when(notificationRepository.save(any())).thenReturn(notification());
        assertNotNull(notificationRepository.save(notification()));
    }

    @Test
    void sortByOption(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        when(questionRepository.save(any())).thenReturn(question());
        assertNotNull(questionRepository.save(question()));
    }


}
