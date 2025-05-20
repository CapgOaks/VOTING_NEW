package com.capgemini.security4;

import com.capgemini.security4.entity.Users;
import com.capgemini.security4.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(userService);
    }

    @Test
    void testGetAllUsers() throws Exception {
        Users user = new Users();
        user.setUserId(1L);
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value(1L));
    }

    @Test
    void testCreateUser_Success() throws Exception {
        Users user = new Users();
        user.setUserId(1L);
        user.setUserName("testuser");
        user.setUserEmail("test@example.com");
        user.setPasswordHash("password123");
        user.setRole("user");
        Mockito.when(userService.createUser(any(Users.class))).thenReturn(user);

        String json = """
            {
                "userName": "testuser",
                "userEmail": "test@example.com",
                "passwordHash": "password123",
                "role": "user"
            }
        """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/users/1"))
                .andExpect(jsonPath("$.userId").value(1L));
    }


    @Test
    void testUpdateUser_Success() throws Exception {
        Users user = new Users();
        user.setUserId(1L);
        user.setUserName("updated");
        user.setUserEmail("test@example.com");
        user.setPasswordHash("password123");
        user.setRole("admin");
        Mockito.when(userService.updateUser(eq(1L), any(Users.class))).thenReturn(user);

        String json = """
            {
                "userName": "updated",
                "userEmail": "test@example.com",
                "passwordHash": "password123",
                "role": "admin"
            }
        """;

        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("updated"));
    }

    

    @Test
    void testDeleteUser() throws Exception {
        Mockito.doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetUserById() throws Exception {
        Users user = new Users();
        user.setUserId(1L);
        Mockito.when(userService.findByUserId(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1L));
    }

    @Test
    void testCheckIfUserIdExists() throws Exception {
        Mockito.when(userService.existsByUserId(1L)).thenReturn(true);

        mockMvc.perform(get("/api/users/existsByUserId")
                .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @TestConfiguration
    static class UserControllerTestConfig {
        @Bean
        @Primary
        public UserService userService() {
            return Mockito.mock(UserService.class);
        }
    }
}