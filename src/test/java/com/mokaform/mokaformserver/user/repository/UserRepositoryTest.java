package com.mokaform.mokaformserver.user.repository;

import com.mokaform.mokaformserver.config.QuerydslTestConfig;
import com.mokaform.mokaformserver.user.domain.Role;
import com.mokaform.mokaformserver.user.domain.User;
import com.mokaform.mokaformserver.user.domain.enums.AgeGroup;
import com.mokaform.mokaformserver.user.domain.enums.Gender;
import com.mokaform.mokaformserver.user.domain.enums.Job;
import com.mokaform.mokaformserver.user.domain.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslTestConfig.class)
class UserRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private UserRepository userRepository;

    private Role role;

    private User user;

    private final String USER_EMAIL = "test1@gmail.com";
    private final String USER_NICKNAME = "testUser1";
    private final String USER_PASSWORD = "testPassword";

    @BeforeEach
    void setUp() {
        role = new Role(RoleName.USER);
        em.persist(role);

        user = User.builder()
                .email(USER_EMAIL)
                .password(USER_PASSWORD)
                .nickname(USER_NICKNAME)
                .ageGroup(AgeGroup.FIFTIES)
                .gender(Gender.FEMALE)
                .job(Job.FREELANCER)
                .profileImage("https://profile-image.jpg")
                .roles(List.of(role))
                .build();
        em.persist(user);
    }

    @Nested
    @DisplayName("findByEmail() 테스트")
    class FindByEmailTest {

        @Test
        @DisplayName("존재하지 않는 회원을 email로 조회하는 경우, Optional.empty()를 반환한다.")
        void testFindingNotExistsUser() {
            String notExistEmail = "not.exist@gmail.com";

            Optional<User> maybeUser = userRepository.findByEmail(notExistEmail);

            assertThat(maybeUser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("존재하는 회원을 email로 조회하는 경우, User 엔티티를 반환한다.")
        void testFindingExistsUser() {
            Optional<User> maybeUser = userRepository.findByEmail(USER_EMAIL);

            assertThat(maybeUser.isEmpty()).isFalse();
            assertThat(maybeUser.get())
                    .hasFieldOrPropertyWithValue("email", user.getEmail())
                    .hasFieldOrPropertyWithValue("password", user.getPassword())
                    .hasFieldOrPropertyWithValue("nickname", user.getNickname())
                    .hasFieldOrPropertyWithValue("ageGroup", user.getAgeGroup())
                    .hasFieldOrPropertyWithValue("gender", user.getGender())
                    .hasFieldOrPropertyWithValue("job", user.getJob())
                    .hasFieldOrPropertyWithValue("profileImage", user.getProfileImage());
        }

    }

    @Nested
    @DisplayName("existsByEmail() 테스트")
    class ExistsByEmailTest {

        @Test
        @DisplayName("존재하지 않는 회원을 email로 조회하는 경우, false를 반환한다.")
        void testFindingNotExistsUser() {
            String notExistEmail = "not.exist@gmail.com";

            Boolean result = userRepository.existsByEmail(notExistEmail);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("존재하는 회원을 email로 조회하는 경우, User 엔티티를 반환한다.")
        void testFindingExistsUser() {
            Boolean result = userRepository.existsByEmail(USER_EMAIL);

            assertThat(result).isTrue();
        }

    }

    @Nested
    @DisplayName("existsByNickname() 테스트")
    class ExistsByNicknameTest {

        @Test
        @DisplayName("존재하지 않는 회원을 nickname로 조회하는 경우, false를 반환한다.")
        void testFindingNotExistsUser() {
            String notExistEmail = "not.exist@gmail.com";

            Boolean result = userRepository.existsByEmail(notExistEmail);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("존재하는 회원을 nickname로 조회하는 경우, User 엔티티를 반환한다.")
        void testFindingExistsUser() {
            Boolean result = userRepository.existsByEmail(USER_EMAIL);

            assertThat(result).isTrue();
        }

    }
}