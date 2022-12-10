package com.example.answersfactory.service;

import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.repository.AnswerRepository;
import com.example.answersfactory.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Optional;

import static com.example.answersfactory.model.AnswerDtoMock.answerDto;
import static com.example.answersfactory.model.AnswerMock.answer;
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
    void updateAnswer() {

    }

    @Test
    void deleteAnswer() {
    }

    @Test
    void filterByDate() {
    }

}