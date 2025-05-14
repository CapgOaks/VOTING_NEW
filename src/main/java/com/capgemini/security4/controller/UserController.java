package com.capgemini.security4.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.capgemini.security4.entity.Users;
import com.capgemini.security4.exception.UserNotFoundException;
import com.capgemini.security4.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;

	}

	@GetMapping
	public ResponseEntity<List<Users>> getAllUsers() {
		List<Users> users = userService.getAllUsers();
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}

	@PostMapping
	public ResponseEntity<Users> createUser(@RequestBody Users user) {
		Users saved = userService.createUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).location(URI.create("/api/users/" + saved.getUserId()))
				.body(saved);

	}

	@PutMapping("/{userId}")
	public ResponseEntity<Users> updateUser(@PathVariable Long userId, @RequestBody Users newUser) {
		Users updated = userService.updateUser(userId, newUser);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/{userId}")
	public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
		try {
			Users user = userService.findByUserId(userId);
			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (UserNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/existsByUserId")
    public ResponseEntity<Boolean> checkIfUserIdExists(@RequestParam Long userId) {
        boolean exists = userService.existsByUserId(userId);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

}
