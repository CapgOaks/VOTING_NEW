package com.capgemini.security4;

import com.capgemini.security4.controller.ElectionsController;
import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.ElectionsService;
import com.capgemini.security4.security.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ElectionsController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ElectionsController Test Suite")
class ElectionsControllerTest {

    @TestConfiguration
    static class TestConfig {
        @Bean @Primary
        public ElectionsService electionsService() {
            return mock(ElectionsService.class);
        }

        @Bean @Primary
        public JwtUtils jwtUtils() {
            return mock(JwtUtils.class);
        }

        @Bean @Primary
        public CustomUserDetailsService customUserDetailsService() {
            return mock(CustomUserDetailsService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ElectionsService electionsService;

    private Elections sampleElection;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        sampleElection = new Elections();
        sampleElection.setElectionId(1L);
        sampleElection.setTitle("Test Election");
        sampleElection.setDescription("Test Description");
        sampleElection.setStartDate(LocalDateTime.now());
        sampleElection.setEndDate(LocalDateTime.now().plusDays(1));
        sampleElection.setElectionStatus(true);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    @DisplayName("GET /api/elections - should return all elections")
    void shouldReturnAllElections() throws Exception {
        when(electionsService.getAllElections()).thenReturn(List.of(sampleElection));

        mockMvc.perform(get("/api/elections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Election"));
    }

    @Test
    @DisplayName("GET /api/elections/{id} - should return election by ID")
    void shouldReturnElectionById() throws Exception {
        when(electionsService.getElectionById(1L)).thenReturn(sampleElection);

        mockMvc.perform(get("/api/elections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Election"));
    }

    @Test
    @DisplayName("GET /api/elections/by-status - should return elections by status")
    void shouldReturnElectionsByStatus() throws Exception {
        when(electionsService.getElectionsByStatus(true)).thenReturn(List.of(sampleElection));

        mockMvc.perform(get("/api/elections/by-status")
                        .param("status", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Election"));
    }

    @Test
    @DisplayName("GET /api/elections/upcoming - should return upcoming elections")
    void shouldReturnUpcomingElections() throws Exception {
        when(electionsService.getUpcomingElections()).thenReturn(List.of(sampleElection));

        mockMvc.perform(get("/api/elections/upcoming"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Election"));
    }

    @Test
    @DisplayName("POST /api/elections - should create a new election")
    void shouldCreateElection() throws Exception {
        when(electionsService.createElection(any(Elections.class))).thenReturn(sampleElection);

        mockMvc.perform(post("/api/elections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleElection)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Election"));
    }

    @Test
    @DisplayName("PUT /api/elections/{id} - should update an election")
    void shouldUpdateElection() throws Exception {
        when(electionsService.updateElection(eq(1L), any(Elections.class))).thenReturn(sampleElection);

        mockMvc.perform(put("/api/elections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleElection)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Election"));
    }
}
