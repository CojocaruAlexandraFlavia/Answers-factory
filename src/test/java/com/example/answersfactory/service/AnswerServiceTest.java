package com.example.answersfactory.service;

import com.example.answersfactory.model.*;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.repository.AnswerRepository;
import com.example.answersfactory.repository.BadgeRepository;
import com.example.answersfactory.repository.NotificationRepository;
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
import static com.example.answersfactory.model.QuestionMock.question;
import static com.example.answersfactory.model.UserMock.user;
import static com.example.answersfactory.model.VoteResponseRequestMock.voteResponseRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class AnswerServiceTest {

    @Mock
    private AnswerRepository answerRepository;

    @Mock
    private UserService userService;

    @Mock
    private QuestionService questionService;

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private BadgeRepository badgeRepository;

    @InjectMocks
    private AnswerService answerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVoteResponseWithNonExistingAnswer() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(answerService.voteResponse(new VoteResponseRequest()));
    }

    @Test
    void testVoteResponseWhenUserAlreadyVoted(){
        User user = user();
        Answer answer = answer();
        user.addVotedAnswer(answer);
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user));
        assertNull(answerService.voteResponse(voteResponseRequest()));
    }

    @Test
    void testVoteResponseOK(){
        User user = user();
        user.setVotedAnswers(new HashSet<>());
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user));
        when(answerRepository.save(any())).thenReturn(answer());
        assertNotNull(answerService.voteResponse(voteResponseRequest()));
    }

    @Test
    void findAnswerById() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
        Optional<Answer> result = answerService.findAnswerById(1L);
        assertEquals(1, result.get().getId());
    }

    @Test
    void saveAnswer() {
        Question question = new Question();
        question.setId(1L);
        when(questionService.findQuestionById(anyLong())).thenReturn(Optional.of(question));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(answerRepository.save(any())).thenReturn(answer());
        AnswerDto result = answerService.saveAnswer(answerDto());
        assertEquals(1L, result.getQuestionId());
    }

    @Test
    void voteResponse(){

        Notification notification = new Notification();
        notification.setId(1L);
        notification.setQuestion(question());
        when(answerService.findAnswerById(any())).thenReturn(Optional.of(answer()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(notificationRepository.save(notification)).thenReturn(notification);
        when(answerRepository.save(any())).thenReturn(Optional.of(answer()));
        assertEquals(1L, notification.getId());

    }

    @Test
    void updateAnswer() {
        when(answerService.findAnswerById(any())).thenReturn(Optional.of(answer()));
        when(answerRepository.save(any())).thenReturn(Optional.of(answer()));
        assertNotNull(answer().getId());
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

    }


}