package com.capgemini.security4.security;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.capgemini.security4.entity.Users;
import com.capgemini.security4.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	private UserRepository userRepository;

	public CustomUserDetailsService() {
	}

	@Autowired
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
		
		log.info("Attempting authentication for user: {}", usernameOrEmail);

		Users user = userRepository.findByUserNameOrUserEmail(usernameOrEmail, usernameOrEmail).orElseThrow(
				() -> new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail));
		
		Set<GrantedAuthority> authorities = new HashSet<>();
		String role = user.getRole();
		if (role == null || role.isBlank()) {
			role = "user"; // default role
		}
		authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

		log.info("User found: {}, roles: {}, passwordHash: {}", user.getUserName(), authorities, user.getPasswordHash());

		return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPasswordHash(), authorities);
	}
}




