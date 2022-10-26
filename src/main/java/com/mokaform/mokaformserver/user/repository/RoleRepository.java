package com.mokaform.mokaformserver.user.repository;

import com.mokaform.mokaformserver.user.domain.Role;
import com.mokaform.mokaformserver.user.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}
