package com.mokaform.mokaformserver.user.repository;

import com.mokaform.mokaformserver.config.QuerydslTestConfig;
import com.mokaform.mokaformserver.user.domain.Role;
import com.mokaform.mokaformserver.user.domain.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(QuerydslTestConfig.class)
class RoleRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(RoleName.USER);
        em.persist(role);
    }

    @Nested
    @DisplayName("findByName() 테스트")
    class FindByNameTest {

        @Test
        @DisplayName("존재하는 RoleName을 RoleName으로 조회하는 경우, Role를 반환한다.")
        void testFindingExistsRoleName() {
            Optional<Role> result = roleRepository.findByName(RoleName.USER);

            assertThat(result.isPresent()).isTrue();
        }

    }

}