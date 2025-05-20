package com.capgemini.security4;

import com.capgemini.security4.dto.CandidatesDto;
import com.capgemini.security4.dto.CandidateDto;
import com.capgemini.security4.dto.RunningCandidateDto;
import com.capgemini.security4.service.CandidatesService;
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
class CandidatesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CandidatesService candidatesService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(candidatesService);
    }

    @Test
    void testGetAllCandidates() throws Exception {
        CandidatesDto dto = new CandidatesDto();
        dto.setCandidateId(1L);
        Mockito.when(candidatesService.getAllCandidates()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/candidates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].candidateId").value(1L));
    }

    @Test
    void testGetCandidateById() throws Exception {
        CandidatesDto dto = new CandidatesDto();
        dto.setCandidateId(1L);
        Mockito.when(candidatesService.getCandidatesById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/candidates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.candidateId").value(1L));
    }

    @Test
    void testGetByElection() throws Exception {
        CandidateDto dto = new CandidateDto();
        Mockito.when(candidatesService.getCandidatesByElectionId(1L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/candidates/election/1"))
                .andExpect(status().isOk());
    }


    @Test
    void testUpdateCandidates_Success() throws Exception {
        CandidatesDto dto = new CandidatesDto();
        dto.setCandidateId(1L);
        dto.setUserId(2L);
        dto.setPartyId(3L);
        dto.setElectionId(4L);
        dto.setManifesto("Updated Manifesto");
        Mockito.when(candidatesService.updateCandidates(eq(1L), any(CandidatesDto.class))).thenReturn(dto);

        String json = """
            {
                "userId": 2,
                "partyId": 3,
                "electionId": 4,
                "manifesto": "Updated Manifesto"
            }
        """;

        mockMvc.perform(put("/api/candidates/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.candidateId").value(1L));
    }

    

    @Test
    void testDeleteCandidates() throws Exception {
        Mockito.doNothing().when(candidatesService).deleteCandidates(1L);

        mockMvc.perform(delete("/api/candidates/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetRunningCandidates() throws Exception {
        RunningCandidateDto runningDto = new RunningCandidateDto();
        Mockito.when(candidatesService.getRunningCandidates()).thenReturn(List.of(runningDto));

        mockMvc.perform(get("/api/candidates/running"))
                .andExpect(status().isOk());
    }

    @TestConfiguration
    static class CandidatesControllerTestConfig {
        @Bean
        @Primary
        public CandidatesService candidatesService() {
            return Mockito.mock(CandidatesService.class);
        }
    }
}