package com.capgemini.security4.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.dto.LoginDto;
import com.capgemini.security4.dto.ResponseToken;
import com.capgemini.security4.entity.Users;
import com.capgemini.security4.exception.UserAlreadyExistsException;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.UserService;
import com.capgemini.security4.service.UserServiceImpl;

@RestController
@RequestMapping("/auth")
public class AuthController {

	AuthenticationManager authenticationManager;
	UserServiceImpl userService;
	PasswordEncoder passwordEncoder;
	JwtUtils jwtService;

	@Autowired
	public AuthController(AuthenticationManager authenticationManager, UserServiceImpl userService,
			PasswordEncoder passwordEncoder, JwtUtils jwtService) {
		this.userService = userService;
		this.authenticationManager = authenticationManager;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@RequestBody LoginDto loginDto) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

		if (authentication.isAuthenticated()) {
			Users user = userService.findByUserNameOrUserEmail(loginDto.getUsername(), loginDto.getUsername());
			Map<String, Object> claims = new HashMap<>();
			claims.put("email", user.getUserEmail());
			claims.put("userid", user.getUserId());
			claims.put("usertype", user.getRole());
			String token = jwtService.generateToken(loginDto.getUsername(), claims);
			ResponseToken responseToken = new ResponseToken(token);
			return ResponseEntity.status(HttpStatus.OK).body(responseToken);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not Authorized !!");
	}

	@PostMapping("/register")
	public ResponseEntity<Users> registerUser(@RequestBody Users user) {
		if (userService.existsByUserName(user.getUserName()) || userService.existsByUserEmail(user.getUserEmail()))
			throw new UserAlreadyExistsException("Username or Email Exists !");
		user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

		return ResponseEntity.status(HttpStatus.OK).body(userService.createUser(user));
	}
}
