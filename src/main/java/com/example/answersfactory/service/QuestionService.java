package com.example.answersfactory.service;
import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.repository.QuestionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.example.answersfactory.model.dto.QuestionDto.convertEntityToDto;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final UserService userService;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, UserService userService) {
        this.questionRepository = questionRepository;
        this.userService = userService;
    }

    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public QuestionDto saveQuestion(@NotNull QuestionDto questionDto){
        Question question = new Question();
        Optional<User> optionalUser = userService.findUserById(questionDto.getUserId());
         if(optionalUser.isPresent()){
            question.setMessage(questionDto.getMessage());
            question.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            question.setStatus("");
            question.setUser(optionalUser.get());
            question.setNotifications(questionDto.getNotifications());
            question.setAnswers(questionDto.getAnswers());
            question.setSuggestions(questionDto.getSuggestions());
            questionDto.setTopic(questionDto.getTopic());
            question = questionRepository.save(question);
            return convertEntityToDto(question);
       }
        return null;
    }

    public QuestionDto updateQuestion(Long questionId, QuestionDto questionDto){
        Optional<Question> optionalQuestion = findQuestionById(questionId);
        Optional<User> optionalUser = userService.findUserById(questionDto.getUserId());
        if(optionalQuestion.isPresent() && optionalUser.isPresent()){
            Question question = optionalQuestion.get();
            question.setMessage(questionDto.getMessage());
            question.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            question.setStatus(questionDto.getStatus());
            question.setUser(optionalUser.get());
            question.setNotifications(questionDto.getNotifications());
            question.setAnswers(questionDto.getAnswers());
            question.setSuggestions(questionDto.getSuggestions());
            questionDto.setTopic(questionDto.getTopic());
            return convertEntityToDto(questionRepository.save(question));
        }
        return null;
    }

    public boolean deleteQuestion(Long questionId){
        if(questionId != null){
            questionRepository.deleteById(questionId);
            return true;
        }
        return false;
    }
}
