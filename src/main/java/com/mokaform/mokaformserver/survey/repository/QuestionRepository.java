package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
