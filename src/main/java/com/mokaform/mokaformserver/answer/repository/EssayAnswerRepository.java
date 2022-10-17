package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.domain.EssayAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EssayAnswerRepository extends JpaRepository<EssayAnswer, Long> {

    @Query("SELECT ea FROM EssayAnswer ea WHERE ea.answer.answerId = :answerId")
    Optional<EssayAnswer> findByAnswerId(@Param("answerId") Long answerId);
}
