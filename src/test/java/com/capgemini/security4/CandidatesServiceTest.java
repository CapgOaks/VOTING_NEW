package com.capgemini.security4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.repository.CandidatesRepository;
import com.capgemini.security4.service.CandidatesServiceImpl;

@ExtendWith(MockitoExtension.class) 
class CandidatesServiceTest {
	@Mock
    private CandidatesRepository candidatesRepository;

    @InjectMocks
    private CandidatesServiceImpl candidatesService;
    
    @Test
    @DisplayName("Should return candidate when found by ID")
    void shouldReturnCandidateWhenFound() {
        Long candidateId = 1L;
        Candidates mockCandidate = new Candidates();
        mockCandidate.setCandidateId(candidateId);
        mockCandidate.setManifesto("Test Manifesto");
        
        when(candidatesRepository.findById(candidateId))
            .thenReturn(Optional.of(mockCandidate));

        // Act
        Candidates result = candidatesService.getCandidatesById(candidateId);

        // Assert
        assertNotNull(result, "Returned candidate should not be null");
        assertEquals(candidateId, result.getCandidateId(), "Candidate ID should match");
        assertEquals("Test Manifesto",result.getManifesto());
    }
}
