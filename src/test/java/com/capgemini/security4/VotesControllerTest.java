package com.capgemini.security4;



import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.security4.controller.VotesController;
import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.service.VotesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers =VotesController.class)
@AutoConfigureMockMvc(addFilters = false)
class VotesControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private VotesService votesService;
    
    private ObjectMapper objectMapper = new ObjectMapper();
    
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