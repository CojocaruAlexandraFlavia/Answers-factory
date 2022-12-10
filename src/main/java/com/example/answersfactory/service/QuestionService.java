package com.example.answersfactory.service;
import com.example.answersfactory.enums.NotificationStatus;
import com.example.answersfactory.enums.NotificationType;
import com.example.answersfactory.enums.QuestionStatus;
import com.example.answersfactory.enums.TopicValue;
import com.example.answersfactory.model.*;
import com.example.answersfactory.model.dto.NotificationDto;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.repository.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.*;

import static com.example.answersfactory.model.dto.QuestionDto.convertEntityToDto;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final TopicRepository topicRepository;
    private final SuggestionRepository suggestionRepository;
    private final BadgeRepository badgeRepository;
    private final AnswerRepository answerRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, UserService userService,
                           TopicRepository topicRepository,
                           SuggestionRepository suggestionRepository, BadgeRepository badgeRepository, AnswerRepository answerRepository, NotificationRepository notificationRepository) {
        this.questionRepository = questionRepository;
        this.userService = userService;
        this.topicRepository = topicRepository;
        this.suggestionRepository = suggestionRepository;
        this.badgeRepository = badgeRepository;
        this.answerRepository = answerRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public List<Question> findAll(){
        return questionRepository.findAll();
    }

    public QuestionDto saveQuestion(@NotNull QuestionDto questionDto){
        Question question = new Question();
        Optional<User> optionalUser = userService.findUserById(questionDto.getUserId());
        TopicValue t = TopicValue.valueOf(questionDto.getTopic().toUpperCase(Locale.ROOT));
        Optional<Topic> optionalTopic = topicRepository.findByName(t);

        if(optionalUser.isPresent()){
            question.setMessage(questionDto.getMessage());
            question.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            question.setStatus(QuestionStatus.OPEN);
            question.setUser(optionalUser.get());
            if(optionalTopic.isPresent()){
                question.setTopic(optionalTopic.get());
            }
            else{
                Topic topic = new Topic();
                topic.setName(TopicValue.valueOf(questionDto.getTopic().toUpperCase()));
                topic = topicRepository.save(topic);
                question.setTopic(topic);
            }
            question = questionRepository.save(question);
            return convertEntityToDto(question);
        }
        return null;
    }

    public QuestionDto updateQuestion(Long questionId, QuestionDto questionDto){
        Optional<Question> optionalQuestion = findQuestionById(questionId);
        if(optionalQuestion.isPresent()){
            Question question = optionalQuestion.get();
            question.setMessage(questionDto.getMessage());
            return convertEntityToDto(questionRepository.save(question));
        }
        return null;
    }

    public boolean deleteQuestion(Long questionId){
        if(questionId != null){
            Optional<Question> optionalQuestion = findQuestionById(questionId);
            if(optionalQuestion.isPresent()){
                questionRepository.delete(optionalQuestion.get());
                return true;
            }
        }
        return false;
    }

    public void deleteTopic(Long topicId){
        topicRepository.deleteById(topicId);
    }

    public QuestionDto addAnswer(Long questionId, Long userId, String answer){
        Optional<Question> optionalQuestion = findQuestionById(questionId);
        Optional<User> optionalUser = userService.findUserById(userId);
        if(optionalQuestion.isPresent() && optionalUser.isPresent()){
            Question question = optionalQuestion.get();
            Answer a = new Answer();
            a.setMessage(answer);
            a.setQuestion(question);
            a.setUser(optionalUser.get()); //not like this
            a.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            answerRepository.save(a);
           // question.getAnswers().add(a);
            return convertEntityToDto(questionRepository.save(question));
        }
        return null;
    }
    public QuestionDto markAcceptedAnswer(Long questionId, Long userId, Long answerId){
        Optional<Question> optionalQuestion = findQuestionById(questionId);
        Optional<User> optionalUser = userService.findUserById(userId);
        if(optionalQuestion.isPresent() && optionalUser.isPresent()){
            Question question = optionalQuestion.get();
            if(question.getUser().getId().equals(optionalUser.get().getId())){
                Optional<Answer> optionalAnswer = answerRepository.findById(answerId);
                if(optionalAnswer.isPresent() && optionalAnswer.get().getQuestion().getId().equals(questionId)){
                    Answer a = optionalAnswer.get();
                    a.setAcceptedStatus(true);
                    answerRepository.save(a);
                    if(a.getLikes() <= 99){
                        Optional<User> ratedUser = userService.findUserById(a.getUser().getId());
                        if(ratedUser.isPresent()){
                            AnswerService.ReceiveBadge(ratedUser, userService, badgeRepository);
                        }
                    }
                    if(a.getLikes() >= 100){
                        if(!question.getStatus().equals(QuestionStatus.CLOSED)){
                            Notification acceptAnswerReminder = new Notification();
                            acceptAnswerReminder.setQuestion(a.getQuestion());
                            acceptAnswerReminder.setNotificationType(NotificationType.REMINDER_CLOSE_QUESTION);
                            acceptAnswerReminder.setNotificationStatus(NotificationStatus.UNSEEN);
                            notificationRepository.save(acceptAnswerReminder);
                        }
                    }
                    return convertEntityToDto(questionRepository.save(question));
                }
            }
        }
        return null;
    }
    public QuestionDto closeQuestion(Long questionId, Long userId){
        Optional<Question> optionalQuestion = findQuestionById(questionId);
        Optional<User> optionalUser = userService.findUserById(userId);
        if(optionalQuestion.isPresent() && optionalUser.isPresent()) {
            Question question = optionalQuestion.get();
            if (question.getUser().getId().equals(optionalUser.get().getId())) {
                for (Answer a: question.getAnswers()) {
                    if(a.isAcceptedStatus() && a.getLikes() >= 100){
                        question.setStatus(QuestionStatus.CLOSED);
                        return convertEntityToDto(questionRepository.save(question));
                    }
                }

            }
        }
        return null;
    }

    public NotificationDto seeNotification(Long notificationId){
        Optional<Notification> optionalNotification = notificationRepository.findById(notificationId);
        if(optionalNotification.isPresent()){
            Notification notification = optionalNotification.get();
            notification.setNotificationStatus(NotificationStatus.SEEN);
            notification = notificationRepository.save(notification);
            return new NotificationDto(notification.getNotificationType().toString(),
                    notification.getQuestion().getId(), notification.getNotificationStatus());
        }
        return null;
    }

    public QuestionDto sortByOption(Long questionId, String option, String type){
        Optional<Question> optionalQuestion = findQuestionById(questionId);
        if(optionalQuestion.isPresent()){
            Question question = optionalQuestion.get();
            List<Answer> sortedAnswers = question.getAnswers();
            if(type.equals("asc")){
                if(option.equals("popularity")){
                    Collections.sort(sortedAnswers, new Answer());
                    question.setAnswers(sortedAnswers);
                }
                if(option.equals("date")) {
                    Collections.sort(sortedAnswers);
                    question.setAnswers(sortedAnswers);
                }
            }
            if(type.equals("desc")){
                if(option.equals("popularity")){
                    Collections.sort(sortedAnswers, Collections.reverseOrder(new Answer()));
                    question.setAnswers(sortedAnswers);
                }
                if(option.equals("date")) {
                    Collections.sort(sortedAnswers, Collections.reverseOrder());
                    question.setAnswers(sortedAnswers);
                }
            }
            return convertEntityToDto(questionRepository.save(question));
        }
        return null;
    }
//    private Boolean checkIfUserQuestionRelationshipExists(Long userId, Long questionId){
//
//    }
}
