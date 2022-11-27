package com.example.answersfactory.controller;

import com.example.answersfactory.model.dto.SuggestionDto;
import com.example.answersfactory.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/suggestion")
public class SuggestionController {

    private final SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService service) {
        this.suggestionService = service;
    }

    @PostMapping("/save")
    public ResponseEntity<SuggestionDto> saveSuggestion(@RequestBody SuggestionDto suggestionDto){
        SuggestionDto result = suggestionService.saveSuggestion(suggestionDto);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
