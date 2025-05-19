package com.capgemini.security4;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
import com.capgemini.security4.repository.UserRepository;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.UserService;
import com.capgemini.security4.service.UserServiceImpl;

@SpringJUnitConfig
public class UserServiceTest {

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
    public void testCreateUser() {
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
    public void testExistsByUserName() {
        String userName = "testuser";
        when(userRepository.existsByUserName(userName)).thenReturn(true);

        boolean exists = userService.existsByUserName(userName);
        assertTrue(exists);
    }

    @Test
    public void testGetAllUsers() {
        List<Users> usersList = new ArrayList<>();
        usersList.add(new Users());
        when(userRepository.findAll()).thenReturn(usersList);

        List<Users> users = userService.getAllUsers();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }
}
