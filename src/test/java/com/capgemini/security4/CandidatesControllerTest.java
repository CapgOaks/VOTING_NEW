package com.capgemini.security4;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.security4.controller.CandidatesController;
import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.entity.Party;
import com.capgemini.security4.entity.Users;
import com.capgemini.security4.security.CustomUserDetailsService;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.CandidatesService;

@WebMvcTest(CandidatesController.class)
@AutoConfigureMockMvc(addFilters = false)
class CandidatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CandidatesService candidatesService;
    
    @MockBean
    private JwtUtils jwtUtils;
    
    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private Candidates candidate;
    private Users user;
    private Party party;
    private Elections election;

    @BeforeEach
    void setUp() {
        user = new Users();
        user.setUserId(2L);
        
        party = new Party();
        party.setPartyId(1L);
        
        election = new Elections();
        election.setElectionId(2L);
        
        candidate = new Candidates();
        candidate.setCandidateId(1L);
        candidate.setManifesto("Test Manifesto");
        candidate.setUser(user);
        candidate.setParty(party);
        candidate.setElection(election);
    }

    @Test
    @DisplayName("GET /api/candidates - Success")
    void shouldGetAllCandidates() throws Exception {
        List<Candidates> candidates = Arrays.asList(candidate);
        when(candidatesService.getAllCandidates()).thenReturn(candidates);
        
        mockMvc.perform(get("/api/candidates"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].candidateId").value(1))
               .andExpect(jsonPath("$[0].manifesto").value("Test Manifesto"))
               .andDo(print());
    }

    @Test
    @DisplayName("POST /api/candidates - Success")
    void shouldCreateCandidate() throws Exception {
        when(candidatesService.createCandidates(any(Candidates.class)))
               .thenReturn(candidate);
        
        String requestBody = """
            {
                "manifesto": "Test Manifesto",
                "user": { "userId": 2 },
                "party": { "partyId": 1 },
                "election": { "electionId": 2 }
            }
            """;

        mockMvc.perform(post("/api/candidates")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.candidateId").value(1))
               .andExpect(jsonPath("$.user.userId").value(2))
               .andExpect(jsonPath("$.party.partyId").value(1))
               .andExpect(jsonPath("$.election.electionId").value(2))
               .andExpect(jsonPath("$.manifesto").value("Test Manifesto"))
               .andDo(print());
    }

    @Test
    @DisplayName("GET /api/candidates/{id} - Success")
    void shouldGetCandidateById() throws Exception {
        when(candidatesService.getCandidatesById(1L))
               .thenReturn(candidate);

        mockMvc.perform(get("/api/candidates/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.candidateId").value(1))
               .andExpect(jsonPath("$.manifesto").value("Test Manifesto"))
               .andDo(print());
    }


    @Test
    @DisplayName("PUT /api/candidates/{id} - Success")
    void shouldUpdateCandidate() throws Exception {
        candidate.setManifesto("Updated Manifesto");
        
        when(candidatesService.updateCandidates(eq(1L), any(Candidates.class)))
               .thenReturn(candidate);
        
        String requestBody = """
            {
                "manifesto": "Updated Manifesto",
                "user": { "userId": 2 },
                "party": { "partyId": 1 },
                "election": { "electionId": 2 }
            }
            """;

        mockMvc.perform(put("/api/candidates/1")
               .contentType(MediaType.APPLICATION_JSON)
               .content(requestBody))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.candidateId").value(1L))
               .andExpect(jsonPath("$.manifesto").value("Updated Manifesto"))
               .andDo(print());
    }

    @Test
    @DisplayName("DELETE /api/candidates/{id} - Success")
    void shouldDeleteCandidate() throws Exception {
        doNothing().when(candidatesService).deleteCandidates(1L);

        mockMvc.perform(delete("/api/candidates/1"))
               .andExpect(status().isNoContent())
               .andDo(print());
    }

}