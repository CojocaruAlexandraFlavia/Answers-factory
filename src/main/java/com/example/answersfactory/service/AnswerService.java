package com.example.answersfactory.service;

import com.example.answersfactory.enums.NotificationStatus;
import com.example.answersfactory.enums.NotificationType;
import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.Notification;
import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.repository.AnswerRepository;
import com.example.answersfactory.repository.NotificationRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.example.answersfactory.model.dto.AnswerDto.convertEntityToDto;

@Service
public class AnswerService {

    private final QuestionService questionService;
    private final AnswerRepository answerRepository;
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    @Autowired
    public AnswerService(QuestionService questionService, AnswerRepository answerRepository,
                         NotificationRepository notificationRepository, UserService userService) {
        this.questionService = questionService;
        this.answerRepository = answerRepository;
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    public Optional<Answer> findAnswerById(Long id) {
        return answerRepository.findById(id);
    }

    public AnswerDto saveAnswer(@NotNull AnswerDto answerDto){
        Answer answer = new Answer();
        Optional<Question> optionalQuestion = questionService.findQuestionById(answerDto.getQuestionId());
        Optional<User> optionalUser = userService.findUserById(answerDto.getUserId());
        if(optionalQuestion.isPresent() && optionalUser.isPresent()){
            //saving answer
            answer.setMessage(answerDto.getMessage());
            answer.setQuestion(optionalQuestion.get());
            answer.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            answer.setUser(optionalUser.get());
            answer = answerRepository.save(answer);

            //send notification
            Notification notification = new Notification();
            notification.setQuestion(optionalQuestion.get());
            notification.setNotificationType(NotificationType.RECEIVED_ANSWER);
            notification.setNotificationStatus(NotificationStatus.UNSEEN);
            notificationRepository.save(notification);

            return convertEntityToDto(answer);
        }
        return null;
    }

    public AnswerDto updateAnswer(Long answerId, AnswerDto answerDto){
        Optional<Answer> optionalAnswer = findAnswerById(answerId);
        if(optionalAnswer.isPresent()){
            Answer answer = optionalAnswer.get();
            answer.setMessage(answerDto.getMessage());
            answer.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            return convertEntityToDto(answerRepository.save(answer));
        }
        return null;
    }

    @Transactional
    public boolean deleteAnswer(Long answerId){
        Optional<Answer> optionalAnswer = findAnswerById(answerId);
        if(optionalAnswer.isPresent()){
            answerRepository.delete(optionalAnswer.get());
            return true;
        }
        return false;
    }

    public AnswerDto voteResponse(@NotNull VoteResponseRequest request){
        Optional<Answer> optionalAnswer = findAnswerById(request.getResponseId());
        Optional<User> optionalUser = userService.findUserById(request.getUserId());
        if(optionalAnswer.isPresent() && optionalUser.isPresent() &&
                (request.getOption() == 0 || request.getOption() == 1)){
            Answer answer = optionalAnswer.get();
            User user = optionalUser.get();
            Optional<Answer> votedAnswer = user.getVotedAnswers().stream().filter(a ->
                    a.getId().equals(request.getResponseId())).findFirst();

            //check if user already voted the response
            if(votedAnswer.isEmpty()){
                if(request.getOption() == 0){
                    answer.setDislikes(answer.getDislikes() + 1);
                } else {
                    answer.setLikes(answer.getLikes() + 1);
                }
                user.addVotedAnswer(answer);
                userService.insertUser(user);
                answer = answerRepository.save(answer);
                return convertEntityToDto(answer);
            }
        }
        return null;
    }
}
