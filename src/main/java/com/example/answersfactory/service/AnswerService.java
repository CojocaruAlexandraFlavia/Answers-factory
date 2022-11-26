package com.example.answersfactory.service;

import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.repository.AnswerRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.example.answersfactory.model.dto.AnswerDto.convertEntityToDto;

@Service
public class AnswerService {

    private final QuestionService questionService;
    private final AnswerRepository answerRepository;
    private final UserService userService;

    @Autowired
    public AnswerService(QuestionService questionService, AnswerRepository answerRepository, UserService userService) {
        this.questionService = questionService;
        this.answerRepository = answerRepository;
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
            answer.setMessage(answerDto.getMessage());
            answer.setQuestion(optionalQuestion.get());
            answer.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            answer.setUser(optionalUser.get());
            answer = answerRepository.save(answer);
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

    public boolean deleteAnswer(Long answerId){
        if(answerId != null){
            answerRepository.deleteById(answerId);
            return true;
        }
        return false;
    }

    public AnswerDto voteResponse(Long responseId, int option){
        Optional<Answer> optionalAnswer = findAnswerById(responseId);
        if(optionalAnswer.isPresent() && (option == 0 || option == 1)){
            Answer answer = optionalAnswer.get();
            if(option == 0){
                answer.setDislikes(answer.getDislikes() + 1);
            } else {
                answer.setLikes(answer.getLikes() + 1);
            }
            answer = answerRepository.save(answer);
            return convertEntityToDto(answer);
        }
        return null;
    }
}
