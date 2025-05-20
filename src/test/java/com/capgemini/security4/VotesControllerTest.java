package com.capgemini.security4;

import com.capgemini.security4.dto.VoteRequestDto;
import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.entity.Users;
import com.capgemini.security4.entity.Votes;

import com.capgemini.security4.service.CandidatesService;
import com.capgemini.security4.service.ElectionsService;
import com.capgemini.security4.service.UserService;
import com.capgemini.security4.service.VotesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("VotesController Test Suite")
class VotesControllerTest {

    @TestConfiguration
    static class VotesControllerTestConfig {
        @Bean @Primary public VotesService votesService() { return Mockito.mock(VotesService.class); }
        @Bean @Primary public UserService userService() { return Mockito.mock(UserService.class); }
        @Bean @Primary public CandidatesService candidatesService() { return Mockito.mock(CandidatesService.class); }
        @Bean @Primary public ElectionsService electionsService() { return Mockito.mock(ElectionsService.class); }
    }

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private VotesService votesService;
    @Autowired


    private ObjectMapper objectMapper;
    private Users user;
    private Candidates candidate;
    private Elections election;
    private Votes vote;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();

        user = new Users();
        user.setUserId(1L);
        user.setUserName("testuser");

        candidate = new Candidates();
        candidate.setCandidateId(2L);

        election = new Elections();
        election.setElectionId(3L);

        vote = new Votes();
        vote.setVoteId(10L);
        vote.setUser(user);
        vote.setCandidate(candidate);
        vote.setElection(election);
        vote.setTimeStamp(LocalDateTime.now());
    }

    @Test
    @DisplayName("GET /api/votes/user/{userId} - should return vote status")
    void shouldReturnUserVoteStatus() throws Exception {
        Mockito.when(votesService.hasUserVoted(1L)).thenReturn(true);

        mockMvc.perform(get("/api/votes/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }


    @Test
    @DisplayName("POST /api/votes - should return 400 on validation error")
    void shouldReturn400OnValidationError() throws Exception {
        VoteRequestDto dto = new VoteRequestDto(null, null); // missing required fields

        mockMvc.perform(post("/api/votes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /api/votes/election/{electionId} - should return election results")
    void shouldReturnElectionResults() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("candidateId", 2L);
        result.put("votes", 100L);

        Mockito.when(votesService.getElectionResults(anyLong())).thenReturn(List.of(result));

        mockMvc.perform(get("/api/votes/election/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].candidateId").value(2L))
                .andExpect(jsonPath("$[0].votes").value(100L));
    }
}