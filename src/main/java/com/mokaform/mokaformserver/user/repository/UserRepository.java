package com.mokaform.mokaformserver.user.repository;

import com.mokaform.mokaformserver.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


}
