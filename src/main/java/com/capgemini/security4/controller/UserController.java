package com.capgemini.security4.controller;

import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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

import com.capgemini.security4.service.UserService;


import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserController {

	private final UserService userService;

	@Autowired
	public UserController(UserService userService) {
		this.userService = userService;

	}

	@GetMapping
	public ResponseEntity<List<Users>> getAllUsers() {
		log.info("GET /api/users - Fetching all users");
		List<Users> users = userService.getAllUsers();
		log.info("Fetched {} user(s)", users.size());
		return ResponseEntity.status(HttpStatus.OK).body(users);
	}
  
  @PostMapping
  public ResponseEntity<Users> createUser(@Valid @RequestBody Users user, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
    }

    log.info("POST /api/users - Creating user: {}", user);
    Users saved = userService.createUser(user);
    log.info("User created with ID: {}", saved.getUserId());

    return ResponseEntity
            .created(URI.create("/api/users/" + saved.getUserId()))
            .body(saved);
  }
  
  @PutMapping("/{userId}")
  public ResponseEntity<Users> updateUser(@PathVariable Long userId,
                                        @Valid @RequestBody Users newUser,
                                        BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
        throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
    }

    log.info("PUT /api/users/{} - Updating user with new data: {}", userId, newUser);
    Users updated = userService.updateUser(userId, newUser);
    log.info("User with ID {} updated successfully", userId);

    return ResponseEntity.ok(updated);
  }

	@DeleteMapping("/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		log.info("DELETE /api/users/{} - Deleting user", userId);
		userService.deleteUser(userId);
		log.info("User with ID {} deleted", userId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}


    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable Long userId) {
        Users user = userService.findByUserId(userId); 
        return ResponseEntity.ok(user);
    }
	
    @GetMapping("/existsByUserId")
    public ResponseEntity<Boolean> checkIfUserIdExists(@RequestParam Long userId) {
        boolean exists = userService.existsByUserId(userId);
        return ResponseEntity.ok(exists);
    }

}
