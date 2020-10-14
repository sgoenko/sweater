package com.hay.sweater.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hay.sweater.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

	User findByActivationCode(String code);
}
