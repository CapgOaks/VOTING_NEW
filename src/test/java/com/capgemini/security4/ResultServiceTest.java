package com.capgemini.security4;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.entity.Results;
import com.capgemini.security4.exception.ElectionNotFoundException;
import com.capgemini.security4.exception.ResultNotFoundException;
import com.capgemini.security4.repository.ElectionsRepository;
import com.capgemini.security4.repository.ResultsRepository;
import com.capgemini.security4.repository.VotesRepository;
import com.capgemini.security4.repository.VotesRepository.WinnerProjection;
import com.capgemini.security4.service.ResultsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class ResultServiceTest {

    @Mock
    private ResultsRepository resultsRepository;
    @Mock
    private VotesRepository votesRepository;
    @Mock
    private ElectionsRepository electionsRepository;

    @InjectMocks
    private ResultsServiceImpl resultsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateResult() {
        Results result = new Results();
        when(resultsRepository.save(result)).thenReturn(result);
        Results saved = resultsService.createResult(result);
        assertNotNull(saved);
    }

    @Test
    void testUpdateResult_Success() {
        Results existing = new Results();
        existing.setResultId(1L);
        Results update = new Results();
        update.setCandidateId(2L);
        update.setElectionId(3L);
        update.setTotalVotes(100L);
        update.setDeclaredAt(LocalDateTime.now());
        when(resultsRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(resultsRepository.save(any())).thenReturn(existing);
        Results updated = resultsService.updateResult(1L, update);
        assertNotNull(updated);
    }

    @Test
    void testUpdateResult_NotFound() {
        Results update = new Results();
        when(resultsRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResultNotFoundException.class, () -> resultsService.updateResult(99L, update));
    }

    @Test
    void testDeleteResult_Success() {
        Results result = new Results();
        when(resultsRepository.findById(1L)).thenReturn(Optional.of(result));
        doNothing().when(resultsRepository).delete(result);
        resultsService.deleteResult(1L);
        verify(resultsRepository).delete(result);
    }

    @Test
    void testDeleteResult_NotFound() {
        when(resultsRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResultNotFoundException.class, () -> resultsService.deleteResult(99L));
    }

    @Test
    void testFindResult_Success() {
        Results result = new Results();
        when(resultsRepository.findById(1L)).thenReturn(Optional.of(result));
        Results found = resultsService.findResult(1L);
        assertNotNull(found);
    }

    @Test
    void testFindResult_NotFound() {
        when(resultsRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ResultNotFoundException.class, () -> resultsService.findResult(99L));
    }

    @Test
    void testFindAllResults() {
        when(resultsRepository.findAll()).thenReturn(List.of(new Results()));
        List<Results> results = resultsService.findAllResults();
        assertEquals(1, results.size());
    }

    @Test
    void testFindResultsByElectionId() {
        when(resultsRepository.findAllByElectionId(1L)).thenReturn(List.of(new Results()));
        List<Results> results = resultsService.findResultsByElectionId(1L);
        assertEquals(1, results.size());
    }

    @Test
    void testDeclareResult_Success() {
        Elections election = new Elections();
        election.setElectionId(1L);
        election.setElectionStatus(true);
        WinnerProjection winner = mock(WinnerProjection.class);
        when(winner.getCandidateId()).thenReturn(2L);
        when(winner.getTotalVotes()).thenReturn(100L);

        when(electionsRepository.findById(1L)).thenReturn(Optional.of(election));
        when(votesRepository.findTopCandidateIdAndVotes(1L)).thenReturn(winner);
        when(resultsRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(electionsRepository.save(any())).thenReturn(election);

        Results result = resultsService.declareResult(1L);
        assertNotNull(result);
        assertEquals(2L, result.getCandidateId());
        assertEquals(1L, result.getElectionId());
        assertEquals(100L, result.getTotalVotes());
    }

    @Test
    void testDeclareResult_ElectionNotFound() {
        when(electionsRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ElectionNotFoundException.class, () -> resultsService.declareResult(99L));
    }

    @Test
    void testDeclareResult_AlreadyDeclared() {
        Elections election = new Elections();
        election.setElectionId(1L);
        election.setElectionStatus(false);
        when(electionsRepository.findById(1L)).thenReturn(Optional.of(election));
        assertThrows(IllegalStateException.class, () -> resultsService.declareResult(1L));
    }

    @Test
    void testDeclareResult_NoVotes() {
        Elections election = new Elections();
        election.setElectionId(1L);
        election.setElectionStatus(true);
        when(electionsRepository.findById(1L)).thenReturn(Optional.of(election));
        when(votesRepository.findTopCandidateIdAndVotes(1L)).thenReturn(null);
        assertThrows(IllegalStateException.class, () -> resultsService.declareResult(1L));
    }
}