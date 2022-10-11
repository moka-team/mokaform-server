package com.mokaform.mokaformserver.user.service;

import com.mokaform.mokaformserver.survey.domain.enums.Category;
import com.mokaform.mokaformserver.user.domain.PreferenceCategory;
import com.mokaform.mokaformserver.user.domain.User;
import com.mokaform.mokaformserver.user.domain.enums.AgeGroup;
import com.mokaform.mokaformserver.user.domain.enums.Gender;
import com.mokaform.mokaformserver.user.domain.enums.Job;
import com.mokaform.mokaformserver.user.dto.request.SignupRequest;
import com.mokaform.mokaformserver.user.repository.PreferenceCategoryRepository;
import com.mokaform.mokaformserver.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PreferenceCategoryRepository preferenceCategoryRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PreferenceCategoryRepository preferenceCategoryRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.preferenceCategoryRepository = preferenceCategoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void createUser(SignupRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .ageGroup(AgeGroup.valueOf(request.getAgeGroup()))
                .gender(Gender.valueOf(request.getGender()))
                .job(Job.valueOf(request.getJob()))
                .build();
        userRepository.save(user);

        request.getCategory()
                .forEach(v -> createPreferenceCategory(user, v));
    }

    private void createPreferenceCategory(User user, String categoryValue) {
        PreferenceCategory preferenceCategory = new PreferenceCategory(user, Category.valueOf(categoryValue));
        preferenceCategoryRepository.save(preferenceCategory);
    }
}
