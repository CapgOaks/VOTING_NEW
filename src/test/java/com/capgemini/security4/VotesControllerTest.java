package com.capgemini.security4;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
@WebMvcTest(controllers = VotesController.class)
@AutoConfigureMockMvc(addFilters = false)
class VotesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VotesService votesService;

    @MockBean
    private UserService userService;

    @MockBean
    private CandidatesServiceImpl candidatesService;

    @MockBean
    private ElectionsServiceImpl electionsService;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testCheckUserVoteStatus() throws Exception {
        when(votesService.hasUserVoted(1L)).thenReturn(true);

        mockMvc.perform(get("/api/votes/user/1"))
               .andExpect(status().isOk())
               .andExpect(content().string("true"));
    }

    @Test
    void testCastVote() throws Exception {
        Votes vote = new Votes();
        vote.setVoteId(1L);

        when(votesService.castVote(any())).thenReturn(vote);

        mockMvc.perform(post("/api/votes")
               .contentType(MediaType.APPLICATION_JSON)
               .content(objectMapper.writeValueAsString(vote)))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.voteId").value(1));
    }
}
