package com.capgemini.security4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.security4.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByUserEmail(String email);

	Optional<Users> findByUserNameOrUserEmail(String username, String email);

	Optional<Users> findByUserName(String username);

	boolean existsByUserName(String username);

	boolean existsByUserEmail(String email);
}
