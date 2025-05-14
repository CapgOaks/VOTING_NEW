package com.capgemini.security4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.entity.User;
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
	public User createUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public boolean existsByUserName(String username) {
		return userRepository.existsByUserName(username);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	@Override
	public User findByUserNameOrEmail(String username, String email) {
		return userRepository.findByUserNameOrEmail(username, email)
				.orElseThrow(()->new UserNotFoundException("Username or Email not Found !"));
	}
}
