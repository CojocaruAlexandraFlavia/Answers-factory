package com.example.answersfactory.service;


import com.example.answersfactory.model.*;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.NotificationDto;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

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
import static org.mockito.Mockito.verify;
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
    void findQuestionByIdDto() {
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        QuestionDto result = questionService.findQuestionByIdDto(1L);
        assertEquals(1L, result.getUserId());
    }

    @Test
    void findAllQuestions(){
        List<Question> questions = new ArrayList<>();
        questions.add(question());
        when(questionService.findAll()).thenReturn(questions);
        assertEquals(1, questions.size());
    }
    @Test
    void saveQuestionOptionalUserTrue(){
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto result = questionService.saveQuestion(questionDto());
        assertNotNull(result);
    }

    @Test
    void saveQuestionOptionalUserFalse(){
        when(userService.findUserById(anyLong())).thenReturn(Optional.empty());
        QuestionDto dto = questionService.saveQuestion(questionDto());
        assertNull(dto);
    }

    @Test
    void saveQuestionOptionalTopicFalse(){
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(topicRepository.findByName(any())).thenReturn(Optional.empty());
        when(questionRepository.save(any())).thenReturn(question());
        assertNotNull(questionService.saveQuestion(questionDto()));
    }
    @Test
    void saveQuestionOptionalTopicTrue(){
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic()));
        when(questionRepository.save(any())).thenReturn(question());
        assertNotNull(questionService.saveQuestion(questionDto()));
    }

    @Test
    void saveQuestionNull(){
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto questionDto = questionService.saveQuestion(questionDto());
        assertNull(questionDto);
    }

    @Test
    void saveQuestionNotNull(){
        when(topicRepository.findById(anyLong())).thenReturn(Optional.of(topic()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto result = questionService.saveQuestion(questionDto());
        assertNotNull(result);
    }

    @Test
    void updateQuestionPresent(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto result = questionService.updateQuestion(1L, questionDto());
        assertNotNull(result);
    }

    @Test
    void updateQuestion(){
        when(questionService.findQuestionById(anyLong())).thenReturn(Optional.empty());
        QuestionDto result = questionService.updateQuestion(question().getId(), questionDto());
        assertNull(result);
    }


    @Test
    void deleteQuestionFalse(){
        boolean result = questionService.deleteQuestion(null);
        assertFalse(result);
    }

    @Test
    void deleteQuestionTrue(){
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        assertTrue(questionService.deleteQuestion(1L));
    }

    @Test
    void deleteTopic(){
        Topic topic = topic();
        questionService.deleteTopic(topic.getId());
        verify(topicRepository).deleteById(1L);
    }

//    @Test
//    void markAcceptedAnswerQuestionPresent(){
//        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
//        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user())).thenReturn(Optional.empty());
//        Optional<Question> q = questionService.findQuestionById(question().getId());
//        assertNotNull(q);
//    }
//
//    @Test
//    void markAcceptedAnswerUserPresent(){
//        when(questionRepository.findById(question().getId())).thenReturn(Optional.of(question()));
//        when(questionRepository.save(any())).thenReturn(question());
//        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
//        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
//        QuestionDto result = questionService.markAcceptedAnswer(1L, 1L, 1L);
//        assertNotNull(result);
//    }
//
//    @Test
//    void markAcceptedAnswerPresent(){
//        when(questionRepository.findById(question().getId())).thenReturn(Optional.of(question()));
//        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
//        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
//        Optional<Answer> answer = answerRepository.findById(anyLong());
//        assertNotNull(answer);
//    }
//
//    @Test
//    void markAcceptedAnswerLikes(){
//        when(questionRepository.findById(question().getId())).thenReturn(Optional.of(question()));
//        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
//        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
//        when(answerRepository.save(any())).thenReturn(answer());
//        Optional<Answer> answer = answerRepository.findById(anyLong());
//        assertFalse(answer.get().getLikes() >= 100);
//    }
//    @Test
//    void markAcceptedAnswerLikesTrue(){
//        when(questionRepository.findById(question().getId())).thenReturn(Optional.of(question()));
//        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
//        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
//        when(answerRepository.save(any())).thenReturn(answer());
//        Optional<Answer> answer = answerRepository.findById(anyLong());
//        assertTrue(answer.get().getLikes() <= 99);
//    }



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
    void closeQuestionAnswerNotAccepted(){
        Question question = question();
        Answer answer = answer();
        answer.setAcceptedStatus(false);
        question.setAnswers(Collections.singletonList(answer));
        when(questionService.findQuestionById((anyLong()))).thenReturn(Optional.of(question));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        QuestionDto result = questionService.closeQuestion(1L, 1L);
        assertNull(result);
    }

    @Test
    void closeQuestionUserIdsNotMatching(){
        User user = user();
        user.setId(2L);
        when(questionService.findQuestionById((anyLong()))).thenReturn(Optional.of(question()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user));
        QuestionDto result = questionService.closeQuestion(1L, 1L);
        assertNull(result);
    }

    @Test
    void closeQuestionMatch(){
        when(questionRepository.findById((anyLong()))).thenReturn(Optional.of(question()));
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(user()));
        when(questionRepository.save(any())).thenReturn(question());
        QuestionDto result = questionService.closeQuestion(1L, 1L);
        assertNotNull(result);
    }

    @Test
    void seeNotificationPresent(){
        when(notificationRepository.findById(anyLong())).thenReturn(Optional.of(notification()));
        when(notificationRepository.save(any())).thenReturn(notification());
        NotificationDto dto = questionService.seeNotification(1L);
        assertNotNull(dto);

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
        when(answerRepository.save(any())).thenReturn(answer());

        AnswerDto result = questionService.voteResponse(voteResponseRequest());

        assertNotNull(result);

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
        when(questionRepository.findAll()).thenReturn(new ArrayList<>());
        assertEquals(0, questionService.findAll().size());
    }


    @ParameterizedTest
    @ValueSource(ints = {25, 75, 150, 250})
    void receiveBadge(int correctAnswers){
        Badge badge = new Badge();
        badge.setId(1L);
        badge.setUsers(new HashSet<>());
        User user = user();
        user.setCorrectAnswers(correctAnswers);
        when(badgeRepository.save(badge)).thenReturn(badge);
        questionService.receiveBadge(Optional.of(user));
        verify(badgeRepository).save(any());
    }

    @Test
    void deleteAnswerFalse() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertFalse(questionService.deleteAnswer(1L));
    }

    @Test
    void deleteAnswerTrue() {
        when(answerRepository.findById(anyLong())).thenReturn(Optional.of(answer()));
        assertTrue(questionService.deleteAnswer(1L));
    }

    @Test
    void filterByDate(){
        List<Answer> answerList = new ArrayList<>();
        answerList.add(answer());
        when(answerRepository.findAll()).thenReturn(answerList);
        List<AnswerDto> answers = questionService.filterByDate("week");
        assertEquals(0, answers.size());
    }


    @Test
    void getAllAnswersForQuestionNotEmpty() {
        when(questionRepository.findById(anyLong())).thenReturn(Optional.of(question()));
        assertEquals(1, questionService.getAllAnswersForQuestion(1L).size());
    }

    @Test
    void getAllAnswersForQuestionEmpty() {
        when(questionRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertEquals(0, questionService.getAllAnswersForQuestion(1L).size());
    }

}
