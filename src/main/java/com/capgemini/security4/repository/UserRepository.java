package com.capgemini.security4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.security4.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String email);

	Optional<User> findByUserNameOrEmail(String username, String email);

	Optional<User> findByUserName(String username);

	boolean existsByUserName(String username);

	boolean existsByEmail(String email);
}
