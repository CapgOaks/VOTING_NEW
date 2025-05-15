package com.capgemini.security4.service;

import java.time.LocalDate;
import java.util.List;

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
		user.setCreatedAt(LocalDate.now());
		return userRepository.save(user);
	}

	@Override
	public boolean existsByUserName(String userName) {
		return userRepository.existsByUserName(userName);
	}

	@Override
	public boolean existsByUserEmail(String userEmail) {
		return userRepository.existsByUserEmail(userEmail);
	}

	@Override
	public boolean existsByUserId(Long userId) {
		return userRepository.existsByUserId(userId);
	}

	@Override
	public Users findByUserNameOrUserEmail(String userName, String email) {
		return userRepository.findByUserNameOrUserEmail(userName, email)
				.orElseThrow(() -> new UserNotFoundException("Username or Email not Found !"));
	}

	@Override
	public Users findByUserId(Long userId) {
		return userRepository.findByUserId(userId).orElseThrow(() -> new UserNotFoundException("UserId not found!"));
	}

	@Override
	public Users updateUser(Long userId, Users user) {
		Users existing = userRepository.findByUserId(userId)
				.orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));
		existing.setUserName(user.getUserName());
		existing.setUserEmail(user.getUserEmail());
		existing.setPasswordHash(user.getPasswordHash());
		existing.setDob(user.getDob());
		return userRepository.save(existing);

	}

	@Override
	public void deleteUser(Long userId) {
		if (!userRepository.existsByUserId(userId)) {
			throw new UserNotFoundException("Cannot delete. Job not found with ID: " + userId);
		}
		userRepository.deleteById(userId);
	}

	@Override
	public List<Users> getAllUsers() {
		return userRepository.findAll();
	}
}
