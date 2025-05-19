package com.capgemini.security4;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.capgemini.security4.controller.VotesController;
import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.service.VotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = VotesController.class)
@AutoConfigureMockMvc(addFilters = false)
class VotesControllerTest {

    private final MockMvc mockMvc;
    private final VotesService votesService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public VotesControllerTest(MockMvc mockMvc, VotesService votesService) {
        this.mockMvc = mockMvc;
        this.votesService = votesService;
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public VotesService votesService() {
            return Mockito.mock(VotesService.class);
        }

        @Bean
        public com.capgemini.security4.service.UserService userService() {
            return Mockito.mock(com.capgemini.security4.service.UserService.class);
        }

        @Bean
        public com.capgemini.security4.service.CandidatesServiceImpl candidatesServiceImpl() {
            return Mockito.mock(com.capgemini.security4.service.CandidatesServiceImpl.class);
        }

        @Bean
        public com.capgemini.security4.service.ElectionsServiceImpl electionsServiceImpl() {
            return Mockito.mock(com.capgemini.security4.service.ElectionsServiceImpl.class);
        }

        @Bean
        public com.capgemini.security4.security.JwtUtils jwtUtils() {
            return Mockito.mock(com.capgemini.security4.security.JwtUtils.class);
        }

        @Bean
        public com.capgemini.security4.security.CustomUserDetailsService customUserDetailsService() {
            return Mockito.mock(com.capgemini.security4.security.CustomUserDetailsService.class);
        }
    }

    @Test
    void testCheckUserVoteStatus() throws Exception {
        when(votesService.hasUserVoted(1L)).thenReturn(true);

        mockMvc.perform(get("/api/votes/user/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
    }

    @Test
    void testCastVote() throws Exception {
        // Create a VoteRequestDto with required fields
        class VoteRequestDto {
            public Long electionId;
            public Long candidateId;
            public Long userId;
        }
        VoteRequestDto voteRequestDto = new VoteRequestDto();
        voteRequestDto.electionId = 1L;
        voteRequestDto.candidateId = 1L;
        voteRequestDto.userId = 1L;

        Votes vote = new Votes();
        vote.setVoteId(1L);

        when(votesService.castVote(any())).thenReturn(vote);

        // Mock SecurityContext and Authentication
        org.springframework.security.core.Authentication authentication = mock(org.springframework.security.core.Authentication.class);
        org.springframework.security.core.userdetails.UserDetails mockUserDetails = mock(org.springframework.security.core.userdetails.UserDetails.class);
        when(mockUserDetails.getUsername()).thenReturn("testuser");
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        // Mock static method SecurityUtils.getCurrentUser()
        try (var mockedStatic = org.mockito.Mockito.mockStatic(com.capgemini.security4.security.SecurityUtils.class)) {
            mockedStatic.when(com.capgemini.security4.security.SecurityUtils::getCurrentUser).thenReturn(mockUserDetails);

            mockMvc.perform(post("/api/votes")
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(objectMapper.writeValueAsString(voteRequestDto)))
                   .andExpect(status().isCreated())
                   .andExpect(jsonPath("$.voteId").value(1));
        }

        mockMvc.perform(post("/api/votes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(voteRequestDto)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.voteId").value(1));
    }
}
