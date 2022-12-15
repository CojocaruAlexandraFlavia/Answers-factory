package com.example.answersfactory.controller;

import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static com.example.answersfactory.model.dto.AnswerDto.convertEntityToDto;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final QuestionService questionService;

    @Autowired
    public AnswerController(QuestionService questionService) {
        this.questionService = questionService;
    }


    @PostMapping("/save")
    public ResponseEntity<AnswerDto> saveAnswer(@RequestBody AnswerDto answerDto){
        return ResponseEntity.of(Optional.of(questionService.saveAnswer(answerDto)));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<AnswerDto> findAnswerById(@PathVariable("id") Long id){
        Optional<Answer> optionalAnswer = questionService.findAnswerById(id);
        if(optionalAnswer.isPresent()){
            AnswerDto answerDto = convertEntityToDto(optionalAnswer.get());
            return new ResponseEntity<>(answerDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable("id") Long id){
        boolean result = questionService.deleteAnswer(id);
        if(result){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnswerDto> updateAnswer(@PathVariable("id") Long id, @RequestBody AnswerDto dto){
        AnswerDto result = questionService.updateAnswer(id, dto);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/vote-response")
    public ResponseEntity<AnswerDto> voteResponse(@RequestBody VoteResponseRequest request){
        AnswerDto result = questionService.voteResponse(request);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/filter-by-date/{criteria}")
    public ResponseEntity<List<AnswerDto>> filterResponsesByDate(@PathVariable("criteria") String criteria){
        List<AnswerDto> result = questionService.filterByDate(criteria);
        if(result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/get-all-answers-for-question/{questionId}")
    public ResponseEntity<List<AnswerDto>> getAllAnswersForQuestion(@PathVariable("questionId") Long questionId) {
        List<AnswerDto> result = questionService.getAllAnswersForQuestion(questionId);
        if(result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
