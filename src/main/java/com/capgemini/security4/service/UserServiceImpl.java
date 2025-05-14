package com.capgemini.security4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.entity.Users;
import com.capgemini.security4.exception.UserNotFoundException;
import com.capgemini.security4.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	UserRepository userRepository;

	@Autowired
	public UserServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public Users createUser(Users user) {
		return userRepository.save(user);
	}

	@Override
	public boolean existsByUserName(String username) {
		return userRepository.existsByUserName(username);
	}

	@Override
	public boolean existsByUserEmail(String email) {
		return userRepository.existsByUserEmail(email);
	}
	
	@Override
	public Users findByUserNameOrUserEmail(String username, String email) {
		return userRepository.findByUserNameOrUserEmail(username, email)
				.orElseThrow(()->new UserNotFoundException("Username or Email not Found !"));
	}
}
