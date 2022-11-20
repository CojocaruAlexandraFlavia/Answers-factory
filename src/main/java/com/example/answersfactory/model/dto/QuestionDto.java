package com.example.answersfactory.model.dto;

import com.example.answersfactory.model.Question;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

@Data
public class QuestionDto {

    private String message;
    private String createDate;
    private String status;

    public static @NotNull QuestionDto convertEntityToDto(@NotNull Question question){
        QuestionDto dto = new QuestionDto();
        dto.setStatus(question.getStatus());
        dto.setMessage(question.getMessage());
        dto.setCreateDate(question.getCreateDate());
        return dto;
    }

}
