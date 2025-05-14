package com.capgemini.security4.service;

import com.capgemini.security4.entity.User;

public interface UserService {

	User createUser(User user);

	boolean existsByUserName(String username);

	boolean existsByEmail(String email);

	User findByUserNameOrEmail(String username, String email);
}
