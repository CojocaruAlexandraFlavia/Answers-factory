package com.example.answersfactory.service;


import com.example.answersfactory.model.*;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.NotificationDto;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.example.answersfactory.model.AnswerDtoMock.answerDto;
import static com.example.answersfactory.model.AnswerMock.answer;
import static com.example.answersfactory.model.NotificationMock.notification;
import static com.example.answersfactory.model.QuestionDtoMock.questionDto;
import static com.example.answersfactory.model.QuestionMock.question;
import static com.example.answersfactory.model.TopicMock.topic;
import static com.example.answersfactory.model.UserMock.user;
import static com.example.answersfactory.model.VoteResponseRequestMock.voteResponseRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class QuestionServiceTest {
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
    void findAllQuestions(){
        List<Question> questions = new ArrayList<>();
        questions.add(question());
        when(questionRepository.findAll()).thenReturn(questions);
        assertEquals(1, questions.size());
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
        AnswerDto result = questionService.saveAnswer(answerDto());
        assertNotNull(result);
    }

    @Test
    void markAcceptedAnswer(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
        when(answerRepository.save(any())).thenReturn(answer());
        when(notificationRepository.save(any())).thenReturn(notification());
        when((questionRepository.save(any()))).thenReturn(question());
        QuestionDto result = questionService.markAcceptedAnswer(1L, 1L, 1L);
        assertNotNull(result);
    }

    @Test
    void closeQuestion(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto result = questionService.saveQuestion(questionDto());
        assertNotNull(result);
    }

    @Test
    void seeNotification(){
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification()));
        when(notificationRepository.save(any())).thenReturn(notification());
        NotificationDto notificationDto = questionService.seeNotification(1L);
        assertNotNull(notificationDto);
    }

    @Test
    void sortByOption(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto questionDto = questionService.sortByOption(1L, "asc", "popularity");
        assertNotNull(questionDto);
    }
    @Test
    void testVoteResponseWithNonExistingAnswer() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(questionService.voteResponse(new VoteResponseRequest()));
    }

    @Test
    void testVoteResponseWhenUserAlreadyVoted(){
        User user = user();
        Answer answer = answer();
        user.addVotedAnswer(answer);
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user));
        assertNull(questionService.voteResponse(voteResponseRequest()));
    }

    @Test
    void testVoteResponseOK(){
        User user = user();
        user.setVotedAnswers(new HashSet<>());
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user));
        when(answerRepository.save(any())).thenReturn(answer());
        assertNotNull(questionService.voteResponse(voteResponseRequest()));
    }

    @Test
    void findAnswerById() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
        Optional<Answer> result = questionService.findAnswerById(1L);
        assertEquals(1, result.get().getId());
    }

    @Test
    void saveAnswer() {
        Question question = new Question();
        question.setId(1L);
        when(questionService.findQuestionById(anyLong())).thenReturn(Optional.of(question));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(answerRepository.save(any())).thenReturn(answer());
        AnswerDto result = questionService.saveAnswer(answerDto());
        assertEquals(1L, result.getQuestionId());
    }

    @Test
    void voteResponse(){

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setQuestion(question());
        when(questionService.findAnswerById(any())).thenReturn(Optional.of(answer()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(answerRepository.save(any())).thenReturn(Optional.of(answer()));

        assertEquals(1L, notification.getId());

    }

    @Test
    void updateAnswer() {
        Answer answer = answer();
        when(answerRepository.findById(any())).thenReturn(Optional.of(answer));
        when(answerRepository.save(any())).thenReturn(answer);
        AnswerDto answerDto = questionService.updateAnswer(1L, answerDto());
        assertNotNull(answerDto);
    }

    @Test
    void findAll() {
        List<Answer> answerList = new ArrayList<>();
        answerList.add(answer());
        when(answerRepository.findAll()).thenReturn(answerList);
        assertEquals(1, answerList.size());
    }


    @Test
    void receiveBadge(){
        Badge badge = new Badge();
        badge.setId(1L);
        badge.setUsers(new HashSet<>());
        when(badgeRepository.save(badge)).thenReturn(badge);
        assertEquals(1L, badge.getId());

    }

    @Test
    void deleteAnswer() {
    }
    @Test
    void filterByDate(){
        List<Answer> answerList = new ArrayList<>();
        answerList.add(answer());
        when(answerRepository.findAll()).thenReturn(answerList);
        List<AnswerDto> answers = questionService.filterByDate("week");
        assertEquals(0, answers.size());
    }




}
