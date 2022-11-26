package com.example.answersfactory.service;

import com.example.answersfactory.model.Question;
import com.example.answersfactory.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionService {
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public Optional<Question> findQuestionById(Long id){
        return questionRepository.findById(id);
    }
}
