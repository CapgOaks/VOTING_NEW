package com.capgemini.security4.service;


import java.util.List;

import com.capgemini.security4.entity.Users;

public interface UserService {

	Users createUser(Users user);

	Users findByUserId(Long userId);

	Users updateUser(Long userId, Users user);
	
	void deleteUser(Long userId);
	
	boolean existsByUserId(Long userId);
	
	List<Users> getAllUsers();
}
