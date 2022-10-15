package com.mokaform.mokaformserver.answer.repository;

import com.mokaform.mokaformserver.answer.domain.OXAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OXAnswerRepository extends JpaRepository<OXAnswer, Long> {
}
