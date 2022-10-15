package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.domain.EssayAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssayAnswerRepository extends JpaRepository<EssayAnswer, Long> {
}
