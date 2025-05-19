package com.capgemini.security4;

import com.capgemini.security4.controller.VotesController;
import com.capgemini.security4.dto.VoteRequestDto;
import com.capgemini.security4.entity.*;
import com.capgemini.security4.security.SecurityUtils;
import com.capgemini.security4.service.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

<<<<<<< Updated upstream
import com.capgemini.security4.controller.CandidatesController;
import com.capgemini.security4.controller.VotesController;
import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.security.CustomUserDetailsService;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.CandidatesService;
import com.capgemini.security4.service.CandidatesServiceImpl;
import com.capgemini.security4.service.ElectionsService;
import com.capgemini.security4.service.ElectionsServiceImpl;
import com.capgemini.security4.service.UserService;
import com.capgemini.security4.service.VotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
@WebMvcTest(controllers = VotesController.class)
class VotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotesService votesService;

    @MockBean
    private UserService userService;

    @MockBean
    private CandidatesService candidatesService;

    @MockBean
    private ElectionsService electionsService;

    @Autowired
    private ObjectMapper objectMapper;

    private Users testUser;
    private Candidates testCandidate;
    private Elections testElection;
    private Votes testVote;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setUserId(1L);
        testUser.setUserName("testuser");

        testCandidate = new Candidates();
        testCandidate.setCandidateId(10L);

        testElection = new Elections();
        testElection.setElectionId(100L);

        testVote = new Votes();
        testVote.setVoteId(999L);
        testVote.setUser(testUser);
        testVote.setCandidate(testCandidate);
        testVote.setElection(testElection);
        testVote.setTimeStamp(LocalDateTime.now());
    }

    @Test
    void testCheckUserVoteStatus_UserExists_ReturnsTrue() throws Exception {
        when(userService.existsByUserId(1L)).thenReturn(true);
        when(votesService.hasUserVoted(1L)).thenReturn(true);

        mockMvc.perform(get("/api/votes/user/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testCheckUserVoteStatus_UserNotFound_ThrowsUserNotFoundException() throws Exception {
        when(userService.existsByUserId(1L)).thenReturn(false);

        mockMvc.perform(get("/api/votes/user/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with ID 1 not found"));
    }

    @Test
    void testCastVote_Success() throws Exception {
        VoteRequestDto voteRequestDto = new VoteRequestDto();
        voteRequestDto.setCandidateId(10L);
        voteRequestDto.setElectionId(100L);

        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getCurrentUser)
                    .thenReturn(new User("testuser", "pass", new ArrayList<>()));

            when(userService.findByUserNameOrUserEmail("testuser", "testuser")).thenReturn(testUser);
            when(candidatesService.getCandidateEntityById(10L)).thenReturn(testCandidate);
            when(electionsService.getElectionById(100L)).thenReturn(testElection);
            when(votesService.castVote(any(Votes.class))).thenReturn(testVote);

            mockMvc.perform(post("/api/votes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(voteRequestDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.message").value("Vote successfully cast"))
                    .andExpect(jsonPath("$.voteId").value(999));
        }
    }

    @Test
    void testCastVote_InvalidCandidateOrElection_ReturnsBadRequest() throws Exception {
        VoteRequestDto voteRequestDto = new VoteRequestDto();
        voteRequestDto.setCandidateId(10L);
        voteRequestDto.setElectionId(100L);

        try (MockedStatic<SecurityUtils> mockedStatic = mockStatic(SecurityUtils.class)) {
            mockedStatic.when(SecurityUtils::getCurrentUser)
                    .thenReturn(new User("testuser", "pass", new ArrayList<>()));

            when(userService.findByUserNameOrUserEmail("testuser", "testuser")).thenReturn(testUser);
            when(candidatesService.getCandidateEntityById(10L)).thenReturn(null); // Invalid

            mockMvc.perform(post("/api/votes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(voteRequestDto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    void testGetResultsByElection_ReturnsResults() throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("candidateName", "Alice");
        result.put("votes", 120);

        when(votesService.getElectionResults(100L)).thenReturn(List.of(result));

        mockMvc.perform(get("/api/votes/election/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].candidateName").value("Alice"))
                .andExpect(jsonPath("$[0].votes").value(120));
    }
}
