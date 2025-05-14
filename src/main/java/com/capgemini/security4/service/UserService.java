package com.capgemini.security4.service;

import com.capgemini.security4.entity.Users;

public interface UserService {

	Users createUser(Users user);

	boolean existsByUserName(String username);

	boolean existsByUserEmail(String email);

	Users findByUserNameOrUserEmail(String username, String email);
}
