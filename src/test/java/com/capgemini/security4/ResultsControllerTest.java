package com.capgemini.security4;

import com.capgemini.security4.entity.Results;
import com.capgemini.security4.service.ResultsServiceImpl;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ResultsController Test Suite")
class ResultsControllerTest {

    @TestConfiguration
    static class ResultsServiceTestConfig {
        @Bean
        @Primary
        public ResultsServiceImpl resultsService() {
            return Mockito.mock(ResultsServiceImpl.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ResultsServiceImpl resultsService;

    private Results sampleResult;

    @BeforeEach
    void setup() {
        sampleResult = new Results();
        sampleResult.setResultId(1L);
        sampleResult.setCandidateId(2L);
        sampleResult.setElectionId(10L);
        sampleResult.setTotalVotes(123L);
        sampleResult.setDeclaredAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("GET /api/results - should return all results")
    void shouldReturnAllResults() throws Exception {
        Mockito.when(resultsService.findAllResults()).thenReturn(List.of(sampleResult));

        mockMvc.perform(get("/api/results"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resultId").value(1L));
    }

    @Test
    @DisplayName("GET /api/results/{electionId} - should return results by electionId")
    void shouldReturnResultsByElectionId() throws Exception {
        Mockito.when(resultsService.findResultsByElectionId(10L)).thenReturn(List.of(sampleResult));

        mockMvc.perform(get("/api/results/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].electionId").value(10L));
    }

    @Test
    @DisplayName("POST /api/results/declare/{electionId} - should declare result")
    void shouldDeclareResult() throws Exception {
        Mockito.when(resultsService.declareResult(anyLong())).thenReturn(sampleResult);

        mockMvc.perform(post("/api/results/declare/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.electionId").value(10L));
    }
}