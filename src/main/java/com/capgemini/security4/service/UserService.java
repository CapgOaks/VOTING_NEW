package com.capgemini.security4.service;


import java.util.List;

import com.capgemini.security4.entity.Users;

public interface UserService {

	Users createUser(Users user);

	boolean existsByUserName(String userName);

	boolean existsByUserEmail(String userEmail);

	Users findByUserNameOrUserEmail(String userName, String email);
	
	Users findByUserId(Long userId);

	Users updateUser(Long userId, Users user);
	
	void deleteUser(Long userId);
	
	boolean existsByUserId(Long userId);
	
	List<Users> getAllUsers();
}
