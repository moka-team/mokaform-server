package com.mokaform.mokaformserver.user.repository;

import com.mokaform.mokaformserver.user.domain.PreferenceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceCategoryRepository extends JpaRepository<PreferenceCategory, Long> {
}
