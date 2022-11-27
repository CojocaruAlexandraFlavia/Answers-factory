package com.example.answersfactory.service;

import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.Suggestion;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.SuggestionDto;
import com.example.answersfactory.repository.SuggestionRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.answersfactory.model.dto.SuggestionDto.convertEntityToDto;

@Service
public class SuggestionService {

    private final SuggestionRepository suggestionRepository;
    private final UserService userService;
    private final QuestionService questionService;

    @Autowired
    public SuggestionService(SuggestionRepository suggestionRepository, UserService userService,
                             QuestionService questionService) {
        this.suggestionRepository = suggestionRepository;
        this.userService = userService;
        this.questionService = questionService;
    }

    public SuggestionDto saveSuggestion(@NotNull SuggestionDto dto){
        Optional<User> optionalUser = userService.findUserById(dto.getUserId());
        Optional<Question> optionalQuestion = questionService.findQuestionById(dto.getQuestionId());
        if(optionalUser.isPresent() && optionalQuestion.isPresent()){
            User user = optionalUser.get();
            Question question = optionalQuestion.get();
            Suggestion suggestion = new Suggestion();
            suggestion.setUser(user);
            suggestion.setQuestion(question);
            suggestion.setMessage(dto.getMessage());
            suggestion = suggestionRepository.save(suggestion);
            return convertEntityToDto(suggestion);
        }
        return null;
    }
}
