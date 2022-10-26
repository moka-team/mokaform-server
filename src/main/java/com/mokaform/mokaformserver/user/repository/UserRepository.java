package com.mokaform.mokaformserver.user.repository;

import com.mokaform.mokaformserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndPassword(String email, String password);

    Boolean existsByEmail(String email);
}
