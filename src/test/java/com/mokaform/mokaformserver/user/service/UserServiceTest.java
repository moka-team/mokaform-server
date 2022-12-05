package com.mokaform.mokaformserver.user.service;

import com.mokaform.mokaformserver.common.exception.ApiException;
import com.mokaform.mokaformserver.common.exception.errorcode.CommonErrorCode;
import com.mokaform.mokaformserver.common.exception.errorcode.UserErrorCode;
import com.mokaform.mokaformserver.survey.domain.enums.Category;
import com.mokaform.mokaformserver.user.domain.PreferenceCategory;
import com.mokaform.mokaformserver.user.domain.Role;
import com.mokaform.mokaformserver.user.domain.User;
import com.mokaform.mokaformserver.user.domain.enums.AgeGroup;
import com.mokaform.mokaformserver.user.domain.enums.Gender;
import com.mokaform.mokaformserver.user.domain.enums.Job;
import com.mokaform.mokaformserver.user.domain.enums.RoleName;
import com.mokaform.mokaformserver.user.dto.request.SignupRequest;
import com.mokaform.mokaformserver.user.dto.response.DuplicateValidationResponse;
import com.mokaform.mokaformserver.user.dto.response.UserGetResponse;
import com.mokaform.mokaformserver.user.repository.PreferenceCategoryRepository;
import com.mokaform.mokaformserver.user.repository.RoleRepository;
import com.mokaform.mokaformserver.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PreferenceCategoryRepository preferenceCategoryRepository;

    @Mock
    private RoleRepository roleRepository;

    @Spy
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Role role = new Role(RoleName.USER);

    @Nested
    @DisplayName("createUser() 테스트")
    class CreateUserTest {

        private final String EMAIL = "emily.jung@gmail.com";
        private final String NICKNAME = "jhs";
        private final String PASSWORD = "password12^!";
        private final String AGE_GROUP = AgeGroup.TWENTIES.name();
        private final String GENDER = Gender.MALE.name();
        private final String JOB = Job.STUDENT.name();
        private final String CATEGORY1 = Category.HOBBY.name();
        private final String CATEGORY2 = Category.IT.name();

        private SignupRequest request = SignupRequest.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .ageGroup(AGE_GROUP)
                .gender(GENDER)
                .job(JOB)
                .category(List.of(CATEGORY1, CATEGORY2))
                .build();

        private String encodedPassword = passwordEncoder.encode(request.getPassword());

        @Nested
        @DisplayName("성공 케이스")
        class Success {

            @Test
            @DisplayName("User 저장이 정상적으로 이루어진다.")
            void testSuccessfulUserCreation() {
                // given
                User user = User.builder()
                        .email(request.getEmail())
                        .password(encodedPassword)
                        .nickname(request.getNickname())
                        .ageGroup(AgeGroup.valueOf(request.getAgeGroup()))
                        .gender(Gender.valueOf(request.getGender()))
                        .job(Job.valueOf(request.getJob()))
                        .roles(List.of(role))
                        .build();

                doReturn(Optional.of(role))
                        .when(roleRepository).findByName(role.getName());
                doReturn(encodedPassword)
                        .when(passwordEncoder).encode(any(String.class));
                doReturn(user)
                        .when(userRepository).save(any(User.class));
                doReturn(new PreferenceCategory(user, Category.valueOf(request.getCategory().get(0))),
                        new PreferenceCategory(user, Category.valueOf(request.getCategory().get(1))))
                        .when(preferenceCategoryRepository).save(any(PreferenceCategory.class));

                // when
                userService.createUser(request);

                // then
                verify(roleRepository, times(1)).findByName(any(RoleName.class));
                verify(passwordEncoder, times(1)).encode(any(String.class));
                verify(userRepository, times(1)).save(any(User.class));
                verify(preferenceCategoryRepository, times(2)).save(any(PreferenceCategory.class));
            }

        }

        @Nested
        @DisplayName("실패 케이스")
        class Failure {

            @Test
            @DisplayName("USER Role이 존재하지 않는 경우, RESOURCE_NOT_FOUND Exception이 발생한다.")
            void testNotExistUserRole() {
                // given
                doThrow(new ApiException(CommonErrorCode.RESOURCE_NOT_FOUND))
                        .when(roleRepository).findByName(any(RoleName.class));

                // when
                // then
                assertThatThrownBy(() ->
                        userService.createUser(any(SignupRequest.class)))
                        .isInstanceOf(ApiException.class)
                        .hasMessageContaining(CommonErrorCode.RESOURCE_NOT_FOUND.getMessage());

                verify(userRepository, never()).save(any(User.class));
            }

        }
    }

    @Nested
    @DisplayName("checkEmailDuplication() 테스트")
    class CheckEmailDuplicationTest {

        private String email = "duplicationTest@naver.com";

        @Test
        @DisplayName("이메일 중복 확인이 정상적으로 이루어진다.")
        void testCheckingEmailDuplication() {
            // given
            doReturn(true)
                    .when(userRepository).existsByEmail(email);

            // when
            DuplicateValidationResponse response = userService.checkEmailDuplication(email);

            // then
            assertThat(response)
                    .hasFieldOrPropertyWithValue("isDuplicated", true);

            verify(userRepository, times(1)).existsByEmail(any(String.class));
        }
    }

    @Nested
    @DisplayName("checkNicknameDuplication() 테스트")
    class CheckNicknameDuplicationTest {

        private String nickname = "testNickname";

        @Test
        @DisplayName("닉네임 중복 확인이 정상적으로 이루어진다.")
        void testCheckingNicknameDuplication() {
            // given
            doReturn(true)
                    .when(userRepository).existsByNickname(nickname);

            // when
            DuplicateValidationResponse response = userService.checkNicknameDuplication(nickname);

            // then
            assertThat(response)
                    .hasFieldOrPropertyWithValue("isDuplicated", true);

            verify(userRepository, times(1)).existsByNickname(any(String.class));
        }
    }

    @Nested
    @DisplayName("login() 테스트")
    class LoginTest {

        private String principal = "test@gmail.com";
        private String credentials = "testPassword12!";

        private User user = User.builder()
                .email(principal)
                .password(passwordEncoder.encode(credentials))
                .nickname("nickname")
                .ageGroup(AgeGroup.FIFTIES)
                .gender(Gender.FEMALE)
                .job(Job.FREELANCER)
                .profileImage("https://profile-image.jpg")
                .roles(List.of(role))
                .build();

        @Nested
        @DisplayName("성공 테스트")
        class Success {

            @Test
            @DisplayName("로그인이 정상적으로 성공하여 User를 반환한다.")
            void testSuccessfulLogin() {
                // given
                doReturn(Optional.of(user))
                        .when(userRepository).findByEmailAndIsWithdraw(any(String.class), any(Boolean.class));
                doReturn(true)
                        .when(passwordEncoder).matches(any(String.class), any(String.class));

                // when
                User result = userService.login(principal, credentials);

                // then
                assertThat(result)
                        .hasFieldOrPropertyWithValue("email", user.getEmail())
                        .hasFieldOrPropertyWithValue("password", user.getPassword())
                        .hasFieldOrPropertyWithValue("nickname", user.getNickname())
                        .hasFieldOrPropertyWithValue("ageGroup", user.getAgeGroup())
                        .hasFieldOrPropertyWithValue("gender", user.getGender())
                        .hasFieldOrPropertyWithValue("job", user.getJob())
                        .hasFieldOrPropertyWithValue("profileImage", user.getProfileImage())
                        .hasFieldOrPropertyWithValue("roles", user.getRoles());

                verify(userRepository, times(1)).findByEmailAndIsWithdraw(any(String.class), any(Boolean.class));
                verify(passwordEncoder, times(1)).matches(any(String.class), any(String.class));
            }
        }

        @Nested
        @DisplayName("실패 테스트")
        class Failure {

            @Test
            @DisplayName("email이 일치하지 않는 경우, UsernameNotFoundException()이 발생한다.")
            void testNotMatchEmail() {
                // given
                String invalidPrincipal = "invalid@gmail.com";

                doThrow(new UsernameNotFoundException("Could not found user for " + invalidPrincipal))
                        .when(userRepository).findByEmailAndIsWithdraw(any(String.class), any(Boolean.class));

                // when
                // then
                assertThatThrownBy(() ->
                        userService.login(invalidPrincipal, credentials))
                        .isInstanceOf(UsernameNotFoundException.class)
                        .hasMessageContaining("Could not found user for " + invalidPrincipal);
            }

            @Test
            @DisplayName("password가 일치하지 않는 경우, ApiException(UserErrorCode.INVALID_ACCOUNT_REQUEST)이 발생한다.")
            void testNotMatchPassword() {
                // given
                doReturn(Optional.of(user))
                        .when(userRepository).findByEmailAndIsWithdraw(any(String.class), any(Boolean.class));
                doReturn(false)
                        .when(passwordEncoder).matches(any(String.class), any(String.class));

                // when
                // then
                assertThatThrownBy(() ->
                        userService.login(principal, credentials))
                        .isInstanceOf(ApiException.class)
                        .hasMessageContaining(UserErrorCode.INVALID_ACCOUNT_REQUEST.getMessage());
            }

        }

    }

    @Nested
    @DisplayName("getUserInfo() 테스트")
    class GetUserInfoTest {

        private String email = "test@gmail.com";

        private User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode("pass12!!a"))
                .nickname("nickname")
                .ageGroup(AgeGroup.FIFTIES)
                .gender(Gender.FEMALE)
                .job(Job.FREELANCER)
                .profileImage("https://profile-image.jpg")
                .roles(List.of(role))
                .build();

        @Nested
        @DisplayName("성공 테스트")
        class Success {

            @Test
            @DisplayName("사용자 정보 조회가 정상적으로 성공한다.")
            void testSuccessfulGettingUserInfo() {
                // given
                ReflectionTestUtils.setField(
                        user,
                        User.class,
                        "id",
                        1L,
                        Long.class
                );
                List<Category> categories = List.of(Category.DAILY_LIFE, Category.IT);

                doReturn(Optional.of(user))
                        .when(userRepository).findByEmailAndIsWithdraw(any(String.class), any(Boolean.class));
                doReturn(categories.stream()
                        .map(category ->
                                new PreferenceCategory(user, category))
                        .collect(Collectors.toList()))
                        .when(preferenceCategoryRepository).findByUserId(any(Long.class));

                // when
                UserGetResponse result = userService.getUserInfo(email);

                // then
                assertThat(result)
                        .hasFieldOrPropertyWithValue("userId", user.getId())
                        .hasFieldOrPropertyWithValue("email", user.getEmail())
                        .hasFieldOrPropertyWithValue("nickname", user.getNickname())
                        .hasFieldOrPropertyWithValue("ageGroup", user.getAgeGroup())
                        .hasFieldOrPropertyWithValue("gender", user.getGender())
                        .hasFieldOrPropertyWithValue("job", user.getJob())
                        .hasFieldOrPropertyWithValue("profileImage", user.getProfileImage())
                        .hasFieldOrPropertyWithValue("categories", categories);
            }

        }

        @Nested
        @DisplayName("실패 테스트")
        class Failure {

            @Test
            @DisplayName("존재하지 않는 user의 email인 경우, ApiException(UserErrorCode.USER_NOT_FOUND)이 발생한다.")
            void testNotExistUserEmail() {
                // given
                doThrow(new ApiException(UserErrorCode.USER_NOT_FOUND))
                        .when(userRepository).findByEmailAndIsWithdraw(email, false);

                // when
                // then
                assertThatThrownBy(() ->
                        userService.getUserInfo(email))
                        .isInstanceOf(ApiException.class)
                        .hasMessageContaining(UserErrorCode.USER_NOT_FOUND.getMessage());
            }

        }
    }

}