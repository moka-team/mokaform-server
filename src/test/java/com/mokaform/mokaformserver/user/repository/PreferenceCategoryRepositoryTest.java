package com.mokaform.mokaformserver.user.repository;

import com.mokaform.mokaformserver.config.QuerydslTestConfig;
import com.mokaform.mokaformserver.survey.domain.enums.Category;
import com.mokaform.mokaformserver.user.domain.PreferenceCategory;
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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslTestConfig.class)
class PreferenceCategoryRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private PreferenceCategoryRepository preferenceCategoryRepository;

    private Role role;

    private User user;

    private PreferenceCategory preferenceCategory1;
    private PreferenceCategory preferenceCategory2;

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

        preferenceCategory1 = new PreferenceCategory(user, Category.IT);
        preferenceCategory2 = new PreferenceCategory(user, Category.DAILY_LIFE);
        em.persist(preferenceCategory1);
        em.persist(preferenceCategory2);
    }

    @Nested
    @DisplayName("findByUserId() 테스트")
    class FindByUserIdTest {

        @Test
        @DisplayName("존재하지 않는 회원을 userId로 조회하는 경우, 빈 List를 반환한다.")
        void testFindingNotExistsUser() {
            Long notExistUserId = 10L;

            List<PreferenceCategory> result = preferenceCategoryRepository.findByUserId(notExistUserId);

            assertThat(result.size()).isEqualTo(0);
        }

        @Test
        @DisplayName("존재하는 회원을 userId로 조회하는 경우, PreferenceCategory List를 반환한다.")
        void testFindingExistsUser() {
            List<PreferenceCategory> result = preferenceCategoryRepository.findByUserId(user.getId());

            assertThat(result.size()).isEqualTo(2);

            assertThat(result.get(0))
                    .hasFieldOrPropertyWithValue("category", preferenceCategory1.getCategory())
                    .hasFieldOrPropertyWithValue("user", preferenceCategory1.getUser());
            assertThat(result.get(1))
                    .hasFieldOrPropertyWithValue("category", preferenceCategory2.getCategory())
                    .hasFieldOrPropertyWithValue("user", preferenceCategory2.getUser());
        }

    }

}