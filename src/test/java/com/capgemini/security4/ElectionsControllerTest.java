package com.capgemini.security4;

import com.capgemini.security4.controller.ElectionsController;
import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.ElectionsService;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ElectionsController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ElectionsController Test Suite")
class ElectionsControllerTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public ElectionsService electionsService() {
            return mock(ElectionsService.class);
        }

        @Bean
        @Primary
        public JwtUtils jwtUtils() {
            return mock(JwtUtils.class);
        }

        @Bean
        @Primary
        public com.capgemini.security4.security.CustomUserDetailsService customUserDetailsService() {
            return mock(com.capgemini.security4.security.CustomUserDetailsService.class);
        }
    }

    private final MockMvc mockMvc;
    private final ElectionsService electionsService;

    ElectionsControllerTest(MockMvc mockMvc, ElectionsService electionsService) {
        this.mockMvc = mockMvc;
        this.electionsService = electionsService;
    }

    private Elections sampleElection;

    @BeforeEach
    void setUp() {
        sampleElection = new Elections();
        sampleElection.setElectionId(1L);
        sampleElection.setTitle("Election 1");
        sampleElection.setDescription("Unit test election");
        sampleElection.setStartDate(LocalDateTime.now());
        sampleElection.setEndDate(LocalDateTime.now().plusDays(1));
        sampleElection.setElectionStatus(true);
    }

    @Test
    @DisplayName("GET /api/elections - should return all elections")
    void shouldReturnAllElections() throws Exception {
        when(electionsService.getAllElections()).thenReturn(List.of(sampleElection));

        mockMvc.perform(get("/api/elections"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Election 1"));
    }

    @Test
    @DisplayName("GET /api/elections/{id} - should return one election")
    void shouldReturnElectionById() throws Exception {
        when(electionsService.getElectionById(1L)).thenReturn(sampleElection);

        mockMvc.perform(get("/api/elections/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Election 1"));
    }

    @Test
    @DisplayName("POST /api/elections - should create election")
    void shouldCreateElection() throws Exception {
        when(electionsService.createElection(any(Elections.class))).thenReturn(sampleElection);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(post("/api/elections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleElection)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Election 1"));
    }

    @Test
    @DisplayName("PUT /api/elections/{id} - should update election")
    void shouldUpdateElection() throws Exception {
        when(electionsService.updateElection(eq(1L), any(Elections.class))).thenReturn(sampleElection);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc.perform(put("/api/elections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(sampleElection)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Election 1"));
    }

   

    @Test
    @DisplayName("GET /api/elections/status - should return filtered elections")
    void shouldReturnElectionsByStatus() throws Exception {
        when(electionsService.getElectionsByStatus(true)).thenReturn(List.of(sampleElection));

        mockMvc.perform(get("/api/elections/status")
                        .param("status", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Election 1"));
    }
}
