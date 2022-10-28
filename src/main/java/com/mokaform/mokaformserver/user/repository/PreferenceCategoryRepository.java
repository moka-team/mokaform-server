package com.mokaform.mokaformserver.user.repository;

import com.mokaform.mokaformserver.user.domain.PreferenceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenceCategoryRepository extends JpaRepository<PreferenceCategory, Long> {

    List<PreferenceCategory> findByUserId(Long userId);
}
