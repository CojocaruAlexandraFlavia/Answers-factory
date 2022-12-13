package com.example.answersfactory.controller;

import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.dto.AddAnswerRequest;
import com.example.answersfactory.model.dto.NotificationDto;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.model.dto.*;
import com.example.answersfactory.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.answersfactory.model.dto.QuestionDto.convertEntityToDto;
import static java.util.stream.Collectors.toList;

@RestController
@Transactional
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/save")
    public ResponseEntity<QuestionDto> saveQuestions(@RequestBody QuestionDto questionDto){
        return ResponseEntity.of(Optional.of(questionService.saveQuestion(questionDto)));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<QuestionDto> findQuestionById(@PathVariable("id") Long id){
        Optional<Question> optionalQuestion = questionService.findQuestionById(id);
        if(optionalQuestion.isPresent()){
            QuestionDto questionDto = convertEntityToDto(optionalQuestion.get());
            return new ResponseEntity<>(questionDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get-by-popularity")
    public List<QuestionDto> findAllByPopularity(){
        List<Question> optionalQuestions = questionService.findAll();
        optionalQuestions.sort(new Question());
        if(!optionalQuestions.isEmpty()){
            return optionalQuestions.stream().map(QuestionDto::convertEntityToDto).collect(toList());
        }
        return new ArrayList<>();
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteQuestionById(@PathVariable("id") Long id){
        boolean result = questionService.deleteQuestion(id);
        if(result){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<QuestionDto> updateQuestion(@PathVariable("id") Long id, @RequestBody QuestionDto dto){
        QuestionDto result = questionService.updateQuestion(id, dto);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("{id}/answers-order-by/{option}/{type}")
    public ResponseEntity<QuestionDto> orderAnswers(@PathVariable("id") Long id, @PathVariable("option") String option, @PathVariable String type){

        QuestionDto result = questionService.sortByOption(id, option, type);
        if(result == null){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/mark-accepted-answer")
    public ResponseEntity<QuestionDto> markAcceptedAnswer(@RequestBody MarkAnswerRequest markAnswerRequest){
        QuestionDto result = questionService.markAcceptedAnswer(markAnswerRequest.getQuestionId(), markAnswerRequest.getUserId(), markAnswerRequest.getAnswerId());
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @PutMapping("/close-question")
    public ResponseEntity<QuestionDto> closeQuestion(@RequestBody CloseQuestion closeQuestion){
        QuestionDto result = questionService.closeQuestion(closeQuestion.getQuestionId(), closeQuestion.getUserId());
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PatchMapping("/see-notification/{id}")
    public ResponseEntity<NotificationDto> seeNotification(@PathVariable("id") Long id){
        NotificationDto result = questionService.seeNotification(id);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
