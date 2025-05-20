package com.capgemini.security4;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.capgemini.security4.entity.Users;
import com.capgemini.security4.exception.UserNotFoundException;
import com.capgemini.security4.repository.UserRepository;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.UserService;
import com.capgemini.security4.service.UserServiceImpl;

@SpringJUnitConfig
class UserServiceTest {

	@TestConfiguration
	static class UserServiceTestContextConfiguration {

		@Bean
		public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
			return new UserServiceImpl(userRepository, passwordEncoder);
		}

		@Bean
		public PasswordEncoder passwordEncoder() {
			return new BCryptPasswordEncoder();
		}

		@Bean
		@Primary
		public UserRepository userRepository() {
			return Mockito.mock(UserRepository.class);
		}

		@Bean
		@Primary
		public JwtUtils jwtUtils() {
			return Mockito.mock(JwtUtils.class);
		}
	}

	private final UserService userService;
	private final UserRepository userRepository;

	@Autowired
	public UserServiceTest(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}

	@Test
	void testCreateUser() {
		Users user = new Users();
		user.setUserName("testuser");
		user.setUserEmail("testuser@example.com");
		user.setDob(LocalDate.of(1990, 1, 1));
		user.setPasswordHash("password123");

		when(userRepository.save(user)).thenReturn(user);

		Users createdUser = userService.createUser(user);
		assertNotNull(createdUser);
		assertEquals("testuser", createdUser.getUserName());
	}

	@Test
	void testExistsByUserName() {
		String userName = "testuser";
		when(userRepository.existsByUserName(userName)).thenReturn(true);

		boolean exists = userService.existsByUserName(userName);
		assertTrue(exists);
	}

	@Test
	void testGetAllUsers() {
		List<Users> usersList = new ArrayList<>();
		usersList.add(new Users());
		when(userRepository.findAll()).thenReturn(usersList);

		List<Users> users = userService.getAllUsers();
		assertNotNull(users);
		assertFalse(users.isEmpty());
	}

	@Test
	void testGetUserById() {
		Users user = new Users();
		user.setUserId(1L);
		when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user));
		Users found = userService.findByUserId(1L);
		assertNotNull(found);
		assertEquals(1L, found.getUserId());
	}

	@Test
	void testGetUserById_NotFound() {
		when(userRepository.findById(99L)).thenReturn(java.util.Optional.empty());
		assertThrows(RuntimeException.class, () -> userService.findByUserId(99L));
	}

	@Test
	void testExistsByUserEmail() {
	    String email = "testuser@example.com";
	    when(userRepository.existsByUserEmail(email)).thenReturn(true);
	    boolean exists = userService.existsByUserEmail(email);
	    assertTrue(exists);
	}

	@Test
	void testFindByUserNameOrUserEmail() {
	    Users user = new Users();
	    user.setUserName("testuser");
	    user.setUserEmail("testuser@example.com");
	    when(userRepository.findByUserNameOrUserEmail("testuser", "testuser@example.com"))
	        .thenReturn(Optional.of(user));
	    Users found = userService.findByUserNameOrUserEmail("testuser", "testuser@example.com");
	    assertNotNull(found);
	    assertEquals("testuser", found.getUserName());
	}

	@Test
	void testExistsByUserId() {
	    when(userRepository.existsById(1L)).thenReturn(true);
	    boolean exists = userService.existsByUserId(1L);
	    assertTrue(exists);
	}

	@Test
	void testFindByUserNameOrUserEmail_Success() {
	    Users user = new Users();
	    user.setUserName("testuser");
	    user.setUserEmail("test@example.com");
	    when(userRepository.findByUserNameOrUserEmail("testuser", "test@example.com"))
	        .thenReturn(Optional.of(user));
	    Users found = userService.findByUserNameOrUserEmail("testuser", "test@example.com");
	    assertNotNull(found);
	    assertEquals("testuser", found.getUserName());
	}

	@Test
	void testFindByUserNameOrUserEmail_NotFound() {
	    when(userRepository.findByUserNameOrUserEmail("nouser", "noemail@example.com"))
	        .thenReturn(Optional.empty());
	    assertThrows(UserNotFoundException.class, () ->
	        userService.findByUserNameOrUserEmail("nouser", "noemail@example.com"));
	}

	@Test
	void testUpdateUser_Success() {
	    Users existing = new Users();
	    existing.setUserId(1L);
	    Users update = new Users();
	    update.setUserName("updated");
	    update.setUserEmail("updated@example.com");
	    update.setDob(LocalDate.of(2000, 1, 1));
	    update.setPasswordHash("newpass");
	    when(userRepository.findByUserId(1L)).thenReturn(Optional.of(existing));
	    when(userRepository.save(any())).thenReturn(existing);
	    when(userRepository.save(any())).thenReturn(existing);
	    Users result = userService.updateUser(1L, update);
	    assertNotNull(result);
	}

	@Test
	void testUpdateUser_NotFound() {
	    Users update = new Users();
	    when(userRepository.findByUserId(99L)).thenReturn(Optional.empty());
	    assertThrows(UserNotFoundException.class, () -> userService.updateUser(99L, update));
	}

	@Test
	void testDeleteUser_Success() {
	    when(userRepository.existsByUserId(1L)).thenReturn(true);
	    doNothing().when(userRepository).deleteById(1L);
	    userService.deleteUser(1L);
	    verify(userRepository).deleteById(1L);
	}

	@Test
	void testDeleteUser_NotFound() {
	    when(userRepository.existsByUserId(99L)).thenReturn(false);
	    assertThrows(UserNotFoundException.class, () -> userService.deleteUser(99L));
	}

}
