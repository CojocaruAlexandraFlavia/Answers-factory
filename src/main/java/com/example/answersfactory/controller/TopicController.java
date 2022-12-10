package com.example.answersfactory.controller;

import com.example.answersfactory.service.QuestionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topic")
public class TopicController {

    private final QuestionService questionService;

    public TopicController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @DeleteMapping("/delete-by-id/{id}")
    public void deleteById(@PathVariable("id") Long id){
        questionService.deleteTopic(id);
    }

}
