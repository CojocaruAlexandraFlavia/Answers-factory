package com.example.answersfactory.repository;

import com.example.answersfactory.model.Question;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Modifying(clearAutomatically = true)
    @Query("DELETE from Question q WHERE q.id = ?1")
    void deleteById(@NotNull Long id);


}
