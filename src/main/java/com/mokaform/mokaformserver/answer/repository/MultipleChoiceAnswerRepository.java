package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.domain.MultipleChoiceAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MultipleChoiceAnswerRepository extends JpaRepository<MultipleChoiceAnswer, Long> {
}
