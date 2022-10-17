package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerCustomRepository {
}
