package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.domain.OXAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OXAnswerRepository extends JpaRepository<OXAnswer, Long> {

    @Query("SELECT oxa FROM OXAnswer oxa WHERE oxa.answer.answerId = :answerId")
    Optional<OXAnswer> findByAnswerId(@Param("answerId") Long answerId);
}
