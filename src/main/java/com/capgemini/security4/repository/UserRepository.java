package com.capgemini.security4.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capgemini.security4.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByUserId(Long userId);
	
	boolean existsByUserId(Long userId);

	boolean existsByUserName(String userName);

	boolean existsByUserEmail(String userEmail);

	Optional<Users> findByUserNameOrUserEmail(String userName, String email);

}
