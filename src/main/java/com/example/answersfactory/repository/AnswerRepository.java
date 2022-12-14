package com.example.answersfactory.repository;

import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.Question;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE from Answer q WHERE q.id = ?1")
    void deleteById(@NotNull Long id);

    List<Answer> findByQuestion(Question q);


}
