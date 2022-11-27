package com.example.answersfactory.service;
import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.Suggestion;
import com.example.answersfactory.model.Topic;
import com.example.answersfactory.model.User;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.repository.AnswerRepository;
import com.example.answersfactory.repository.QuestionRepository;
import com.example.answersfactory.repository.SuggestionRepository;
import com.example.answersfactory.repository.TopicRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.answersfactory.model.dto.QuestionDto.convertEntityToDto;

@Service
public class QuestionService {


    private final QuestionRepository questionRepository;
    private final UserService userService;
    private final TopicRepository topicRepository;
    private final AnswerRepository answerRepository;
    private final SuggestionRepository suggestionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, UserService userService, TopicRepository topicRepository, AnswerRepository answerRepository, SuggestionRepository suggestionRepository) {
        this.questionRepository = questionRepository;
        this.userService = userService;
        this.topicRepository = topicRepository;
        this.answerRepository = answerRepository;
        this.suggestionRepository = suggestionRepository;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=true, noRollbackFor=Exception.class)
    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    public QuestionDto saveQuestion(@NotNull QuestionDto questionDto){
        Question question = new Question();
        Optional<User> optionalUser = userService.findUserById(questionDto.getUserId());
        Optional<Topic> optionalTopic = topicRepository.checkIfTopicExists(questionDto.getTopic());

         if(optionalUser.isPresent()){
            question.setMessage(questionDto.getMessage());
            question.setCreateDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            question.setStatus(questionDto.getStatus());
            question.setUser(optionalUser.get());
             if(optionalTopic.isPresent()){
                 question.setTopic(optionalTopic.get());
                 question = questionRepository.save(question);
                 return convertEntityToDto(question);
             }
             else{
                 Topic t = new Topic();
                 t.setName(questionDto.getTopic());
                 topicRepository.save(t);
                 question.setTopic(t);
                 question = questionRepository.save(question);
                 return convertEntityToDto(question);
             }


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
            Optional<Question> question = findQuestionById(questionId);
            if(question.isPresent()){
                Question q = question.get();
                suggestionRepository.deleteAllById(q.getSuggestions().stream().map(Suggestion::getId).collect(Collectors.toList()));
                questionRepository.deleteById(questionId);
                return true;
            }

        }
        return false;
    }
}
