package com.mokaform.mokaformserver.survey.repository;

import com.mokaform.mokaformserver.survey.domain.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long>, SurveyCustomRepository {

    Optional<Survey> findBySharingKey(String sharingKey);

    List<Survey> findAllByUser_Id(Long userId);
}
