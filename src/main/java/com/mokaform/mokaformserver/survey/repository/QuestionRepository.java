package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.domain.QuestionTmp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<QuestionTmp, Long> {
}
