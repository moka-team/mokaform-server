package com.mokaform.mokaformserver.user.service;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.UserErrorCode;
import com.mokaform.mokaformserver.survey.domain.enums.Category;
import com.mokaform.mokaformserver.user.domain.PreferenceCategory;
import com.mokaform.mokaformserver.user.domain.User;
import com.mokaform.mokaformserver.user.domain.enums.AgeGroup;
import com.mokaform.mokaformserver.user.domain.enums.Gender;
import com.mokaform.mokaformserver.user.domain.enums.Job;
import com.mokaform.mokaformserver.user.dto.request.LoginRequest;
import com.mokaform.mokaformserver.user.dto.request.SignupRequest;
import com.mokaform.mokaformserver.user.dto.response.DuplicateValidationResponse;
import com.mokaform.mokaformserver.user.dto.response.LoginResponse;
import com.mokaform.mokaformserver.user.repository.PreferenceCategoryRepository;
import com.mokaform.mokaformserver.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void createUser(SignupRequest request) {
        User user = User.builder()
                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
                .password(request.getPassword())
                .nickname(request.getNickname())
                .ageGroup(AgeGroup.valueOf(request.getAgeGroup()))
                .gender(Gender.valueOf(request.getGender()))
                .job(Job.valueOf(request.getJob()))
                .build();
        userRepository.save(user);

        request.getCategory()
                .forEach(v -> createPreferenceCategory(user, v));
    }

    @Transactional(readOnly = true)
    public LoginResponse getUser(LoginRequest request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(() ->
                        new ApiException(UserErrorCode.USER_NOT_FOUND));
        return new LoginResponse(user);
    }

    @Transactional(readOnly = true)
    public DuplicateValidationResponse checkEmailDuplication(String email) {
        Boolean isDuplicated = userRepository.existsByEmail(email);
        return new DuplicateValidationResponse(isDuplicated);
    }

    private void createPreferenceCategory(User user, String categoryValue) {
        PreferenceCategory preferenceCategory = new PreferenceCategory(user, Category.valueOf(categoryValue));
        preferenceCategoryRepository.save(preferenceCategory);
    }
}
