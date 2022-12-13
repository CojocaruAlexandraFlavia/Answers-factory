package com.example.answersfactory.service;
import com.example.answersfactory.enums.*;
import com.example.answersfactory.model.*;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.NotificationDto;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.repository.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;
import java.util.*;

import static com.example.answersfactory.model.dto.QuestionDto.convertEntityToDto;
import static java.util.stream.Collectors.toList;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final TopicRepository topicRepository;
    private final BadgeRepository badgeRepository;
    private final AnswerRepository answerRepository;
    private final NotificationRepository notificationRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, UserService userService,
                           TopicRepository topicRepository,
                           BadgeRepository badgeRepository, AnswerRepository answerRepository, NotificationRepository notificationRepository) {
        this.questionRepository = questionRepository;
        this.userService = userService;
        this.topicRepository = topicRepository;
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
                            receiveBadge(ratedUser);
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
    public Optional<Answer> findAnswerById(Long id) {
        return answerRepository.findById(id);
    }

    public AnswerDto saveAnswer(@NotNull AnswerDto answerDto){
        Answer answer = new Answer();
        Optional<Question> optionalQuestion = findQuestionById(answerDto.getQuestionId());
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

            return AnswerDto.convertEntityToDto(answer);
        }
        return null;
    }

    public AnswerDto updateAnswer(Long answerId, AnswerDto answerDto){
        Optional<Answer> optionalAnswer = findAnswerById(answerId);
        if(optionalAnswer.isPresent()){
            Answer answer = optionalAnswer.get();
            answer.setMessage(answerDto.getMessage());
            answer.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            return AnswerDto.convertEntityToDto(answerRepository.save(answer));
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
                        receiveBadge(ratedUser);
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
                return AnswerDto.convertEntityToDto(answer);
            }
        }
        return null;
    }

    public void receiveBadge(Optional<User> ratedUser) {
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
