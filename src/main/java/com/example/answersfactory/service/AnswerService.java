package com.example.answersfactory.service;

import com.example.answersfactory.enums.BadgeType;
import com.example.answersfactory.enums.NotificationStatus;
import com.example.answersfactory.enums.NotificationType;
import com.example.answersfactory.enums.QuestionStatus;
import com.example.answersfactory.model.*;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.repository.AnswerRepository;
import com.example.answersfactory.repository.BadgeRepository;
import com.example.answersfactory.repository.NotificationRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.example.answersfactory.model.dto.AnswerDto.convertEntityToDto;
import static java.util.stream.Collectors.toList;

@Service
public class AnswerService {

    private final QuestionService questionService;
    private final AnswerRepository answerRepository;
    private final BadgeRepository badgeRepository;
    private final UserService userService;
    private final NotificationRepository notificationRepository;

    @Autowired
    public AnswerService(QuestionService questionService, AnswerRepository answerRepository,
                         BadgeRepository badgeRepository, NotificationRepository notificationRepository, UserService userService) {
        this.questionService = questionService;
        this.answerRepository = answerRepository;
        this.badgeRepository = badgeRepository;
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
                    Optional<User> ratedUser = userService.findUserById(answer.getUser().getId());
                    if(answer.getLikes() >= 99 && ratedUser.isPresent()){
                        ReceiveBadge(ratedUser, userService, badgeRepository);
                    }
                }

                //send reminder to accept the answer if number of likes is greater than 100
                if(answer.getLikes() >= 100){
                    if(!answer.isAcceptedStatus()){
                        Notification acceptAnswerReminder = new Notification();
                        acceptAnswerReminder.setQuestion(answer.getQuestion());
                        acceptAnswerReminder.setNotificationType(NotificationType.REMINDER_ACCEPT_ANSWER);
                        acceptAnswerReminder.setNotificationStatus(NotificationStatus.UNSEEN);
                        notificationRepository.save(acceptAnswerReminder);
                    }
                    else{
                        if(!answer.getQuestion().getStatus().equals(QuestionStatus.CLOSED)){
                            Notification acceptAnswerReminder = new Notification();
                            acceptAnswerReminder.setQuestion(answer.getQuestion());
                            acceptAnswerReminder.setNotificationType(NotificationType.REMINDER_CLOSE_QUESTION);
                            acceptAnswerReminder.setNotificationStatus(NotificationStatus.UNSEEN);
                            notificationRepository.save(acceptAnswerReminder);
                        }
                    }
                }

                user.addVotedAnswer(answer);
                userService.insertUser(user);
                answer = answerRepository.save(answer);
                return convertEntityToDto(answer);
            }
        }
        return null;
    }

    static void ReceiveBadge(Optional<User> ratedUser, UserService userService, BadgeRepository badgeRepository) {
        if(ratedUser.isPresent()){
            User ratedUser1 = ratedUser.get();
            ratedUser1.setCorrectAnswers(ratedUser1.getCorrectAnswers()+1);
            if(ratedUser1.getCorrectAnswers() >= 10){
                Badge newBadge = new Badge();
                if(ratedUser1.getCorrectAnswers() <= 50){
                    newBadge.setName(BadgeType.JUNIOR);
                }
                if(ratedUser1.getCorrectAnswers() > 50 && ratedUser1.getCorrectAnswers() <= 100){
                    newBadge.setName(BadgeType.MIDDLE);
                }
                if(ratedUser1.getCorrectAnswers() > 100 && ratedUser1.getCorrectAnswers() <= 200){
                    newBadge.setName(BadgeType.SENIOR);
                }
                if(ratedUser1.getCorrectAnswers() > 200){
                    newBadge.setName(BadgeType.EXPERT);
                }
                newBadge.setDate((LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))));
                Set<Badge> userBadges = ratedUser1.getBadges();
                userBadges.add(newBadge);
                badgeRepository.save(newBadge);
                ratedUser1.setBadges(userBadges);

            }

            userService.insertUser(ratedUser1);
        }


    }

    public List<AnswerDto> filterByDate(@NotNull String criteria){
        List<Answer> answers = answerRepository.findAll();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        if(criteria.equalsIgnoreCase("week")){
            answers = answers.stream().filter(answer -> {
                LocalDateTime localDateTime = LocalDateTime.parse(answer.getDate(), dateTimeFormatter);
                LocalDateTime now = LocalDateTime.now();
                WeekFields weekFields = WeekFields.ISO;
                int weekNumberForAnswerDate = localDateTime.get(weekFields.weekOfWeekBasedYear());
                int weekNumberForNow = now.get(weekFields.weekOfWeekBasedYear());
                return localDateTime.getYear() == now.getYear() &&
                        localDateTime.getMonthValue() == now.getMonthValue() &&
                        weekNumberForAnswerDate == weekNumberForNow &&
                        localDateTime.getDayOfWeek().getValue() <= now.getDayOfWeek().getValue();
            }).collect(toList());
        } else if(criteria.equalsIgnoreCase("month")) {
            answers = answers.stream().filter(answer -> {
                LocalDateTime localDateTime = LocalDateTime.parse(answer.getDate(), dateTimeFormatter);
                LocalDateTime now = LocalDateTime.now();
                return  localDateTime.getYear() == now.getYear() &&
                        localDateTime.getMonthValue() ==  now.getMonthValue() &&
                        localDateTime.getDayOfMonth() <= now.getDayOfMonth();
            }).collect(toList());
        } else if (criteria.equalsIgnoreCase("year")) {
            answers = answers.stream().filter(answer ->
                    LocalDateTime.parse(answer.getDate(), dateTimeFormatter).getYear() ==
                            LocalDate.now().getYear()
            ).collect(toList());
        } else
            return new ArrayList<>();
        return answers.stream().map(AnswerDto::convertEntityToDto).collect(toList());
    }
}
