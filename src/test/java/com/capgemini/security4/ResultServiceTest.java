package com.capgemini.security4;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.capgemini.security4.entity.Results;
import com.capgemini.security4.repository.ResultsRepository;
import com.capgemini.security4.repository.VotesRepository;
import com.capgemini.security4.repository.ElectionsRepository;
import com.capgemini.security4.service.ResultsService;
import com.capgemini.security4.service.ResultsServiceImpl;

@SpringJUnitConfig
public class ResultServiceTest {

    @TestConfiguration
    static class ResultServiceTestContextConfiguration {

        @Bean
        public ResultsService resultsService(ResultsRepository resultsRepository, VotesRepository votesRepository,
                ElectionsRepository electionsRepository) {
            return new ResultsServiceImpl(resultsRepository, votesRepository, electionsRepository);
        }

        @Bean
        @Primary
        public ResultsRepository resultsRepository() {
            return Mockito.mock(ResultsRepository.class);
        }

        @Bean
        @Primary
        public VotesRepository votesRepository() {
            return Mockito.mock(VotesRepository.class);
        }

        @Bean
        @Primary
        public ElectionsRepository electionsRepository() {
            return Mockito.mock(ElectionsRepository.class);
        }
    }

    private final ResultsService resultsService;
    private final ResultsRepository resultsRepository;
    

    @Autowired
    public ResultServiceTest(ResultsService resultsService, ResultsRepository resultsRepository,
            VotesRepository votesRepository, ElectionsRepository electionsRepository) {
        this.resultsService = resultsService;
        this.resultsRepository = resultsRepository;
        
    }

    @Test
    public void testCreateResult() {
        Results result = new Results();
        result.setCandidateId(1L);
        result.setElectionId(1L);
        result.setTotalVotes(100L);
        result.setDeclaredAt(LocalDateTime.now());

        when(resultsRepository.save(result)).thenReturn(result);

        Results createdResult = resultsService.createResult(result);
        assertNotNull(createdResult);
        assertEquals(1L, createdResult.getCandidateId());
    }

    @Test
    public void testFindAllResults() {
        List<Results> resultsList = new ArrayList<>();
        resultsList.add(new Results());
        when(resultsRepository.findAll()).thenReturn(resultsList);

        List<Results> results = resultsService.findAllResults();
        assertNotNull(results);
        assertFalse(results.isEmpty());
    }
}
