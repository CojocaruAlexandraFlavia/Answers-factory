package com.example.answersfactory.controller;

import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.dto.AnswerDto;
import com.example.answersfactory.model.dto.VoteResponseRequest;
import com.example.answersfactory.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static com.example.answersfactory.model.dto.AnswerDto.convertEntityToDto;

@RestController
@RequestMapping("/answer")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }


    @PostMapping("/save")
    public ResponseEntity<AnswerDto> saveAnswer(@RequestBody AnswerDto answerDto){
        return ResponseEntity.of(Optional.of(answerService.saveAnswer(answerDto)));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<AnswerDto> findAnswerById(@PathVariable("id") Long id){
        Optional<Answer> optionalAnswer = answerService.findAnswerById(id);
        if(optionalAnswer.isPresent()){
            AnswerDto answerDto = convertEntityToDto(optionalAnswer.get());
            return new ResponseEntity<>(answerDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete-by-id/{id}")
    public ResponseEntity<?> deleteAnswerById(@PathVariable("id") Long id){
        boolean result = answerService.deleteAnswer(id);
        if(result){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AnswerDto> updateAnswer(@PathVariable("id") Long id, @RequestBody AnswerDto dto){
        AnswerDto result = answerService.updateAnswer(id, dto);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/vote-response")
    public ResponseEntity<AnswerDto> voteResponse(@RequestBody VoteResponseRequest request){
        AnswerDto result = answerService.voteResponse(request);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
