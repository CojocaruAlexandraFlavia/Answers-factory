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

            //check if the user who sent the suggestion is not the same as the one who posted the question
            if(!question.getUser().getId().equals(user.getId())){
                Suggestion suggestion = new Suggestion();
                suggestion.setUser(user);
                suggestion.setQuestion(question);
                suggestion.setMessage(dto.getMessage());
                suggestion = suggestionRepository.save(suggestion);
                return convertEntityToDto(suggestion);
            }
        }
        return null;
    }

    public Optional<Suggestion> findSuggestionById(Long id){
        return suggestionRepository.findById(id);
    }

    public boolean deleteSuggestion(Long suggestionId){
        Optional<Suggestion> optionalSuggestion = findSuggestionById(suggestionId);
        if(optionalSuggestion.isPresent()){
            Suggestion suggestion = optionalSuggestion.get();
            suggestion.setQuestion(null);
            suggestionRepository.delete(suggestion);
            return true;
        }
        return false;
    }
}
