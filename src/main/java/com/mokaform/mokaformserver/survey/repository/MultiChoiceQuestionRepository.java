package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.domain.MultipleChoiceQuestion;
import com.mokaform.mokaformserver.survey.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MultiChoiceQuestionRepository extends JpaRepository<MultipleChoiceQuestion, Long> {

    List<MultipleChoiceQuestion> findMultipleChoiceQuestionsByQuestion(Question question);
}
