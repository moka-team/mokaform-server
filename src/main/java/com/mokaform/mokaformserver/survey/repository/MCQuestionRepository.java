package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MCQuestionRepository extends JpaRepository<MultipleChoiceQuestion, Long> {
}