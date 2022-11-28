package com.example.answersfactory.controller;

import com.example.answersfactory.model.Question;
import com.example.answersfactory.model.dto.AddAnswerRequest;
import com.example.answersfactory.model.dto.QuestionDto;
import com.example.answersfactory.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.answersfactory.model.dto.QuestionDto.convertEntityToDto;

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
    @PutMapping("/add-answer")
    public ResponseEntity<QuestionDto> addAnswer(@RequestBody AddAnswerRequest addAnswerRequest){
        QuestionDto result = questionService.addAnswer(addAnswerRequest.getQuestionId(), addAnswerRequest.getUserId(), addAnswerRequest.getMessage());
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);

    }
}
