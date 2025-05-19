package com.capgemini.security4.controller;

import java.util.HashMap;
import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.capgemini.security4.dto.LoginDto;
import com.capgemini.security4.dto.ResponseToken;
import com.capgemini.security4.dto.UserRegistrationDto;
import com.capgemini.security4.entity.Users;
import com.capgemini.security4.exception.UserAlreadyExistsException;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.UserServiceImpl;

import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {

	private final AuthenticationManager authenticationManager;
	private final UserServiceImpl userService;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtService;

	@Autowired
	public AuthController(AuthenticationManager customAuthenticationManager, UserServiceImpl userService,
			PasswordEncoder passwordEncoder, JwtUtils jwtService) {
		this.userService = userService;
		this.authenticationManager = customAuthenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@PostMapping("/signin")
	public ResponseEntity<ResponseToken> authenticateUser(@RequestBody @Valid LoginDto loginDto) {
		log.info("Attempting authentication for user: {}", loginDto.getUserName());
		log.info("Password received: {}", loginDto.getPassword());

		try {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginDto.getUserName(), loginDto.getPassword()));

			if (authentication.isAuthenticated()) {
				log.info("Authentication successful for user: {}", loginDto.getUserName());

				Users user = userService.findByUserNameOrUserEmail(loginDto.getUserName(), loginDto.getUserName());

				Map<String, Object> claims = new HashMap<>();
				claims.put("email", user.getUserEmail());
				claims.put("userid", user.getUserId());
				claims.put("usertype", user.getRole());

				String token = jwtService.generateToken(loginDto.getUserName(), claims);
				log.info("JWT token generated for user: {}", loginDto.getUserName());

				ResponseToken responseToken = new ResponseToken(token);
				return ResponseEntity.ok(responseToken);
			}

			log.warn("Authentication failed for user: {}", loginDto.getUserName());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

		} catch (Exception ex) {
			log.error("Authentication error for user {}: {}", loginDto.getUserName(), ex.getMessage());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@PostMapping("/register")
	public ResponseEntity<Users> registerUser(@RequestBody @Valid UserRegistrationDto userDto) {
		log.info("Registering user: {}", userDto.getUserName());

		if (userDto.getPassword() == null || userDto.getPassword().isBlank()) {
			log.error("Password is null or blank during registration for user: {}", userDto.getUserName());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

		if (userService.existsByUserName(userDto.getUserName())
				|| userService.existsByUserEmail(userDto.getUserEmail())) {
			log.warn("Username or email already exists for: {}", userDto.getUserName());
			throw new UserAlreadyExistsException("Username or Email Exists!");
		}

		Users user = new Users();
		user.setUserName(userDto.getUserName());
		user.setUserEmail(userDto.getUserEmail());
		user.setRole(userDto.getRole());
		user.setDob(userDto.getDob());
		user.setPasswordHash(userDto.getPassword());

		Users savedUser = userService.createUser(user);
		log.info("User registered successfully: {}", savedUser.getUserName());

		return ResponseEntity.ok(savedUser);
	}
}
