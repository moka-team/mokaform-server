package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.domain.MultipleChoiceAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MultipleChoiceAnswerRepository extends JpaRepository<MultipleChoiceAnswer, Long> {

    @Query("SELECT mca FROM MultipleChoiceAnswer mca WHERE mca.answer.answerId = :answerId")
    Optional<MultipleChoiceAnswer> findByAnswerId(@Param("answerId") Long answerId);
}
