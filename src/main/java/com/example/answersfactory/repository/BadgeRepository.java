package com.example.answersfactory.repository;

import com.example.answersfactory.model.Answer;
import com.example.answersfactory.model.Badge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BadgeRepository extends JpaRepository<Badge, Long> {
}
