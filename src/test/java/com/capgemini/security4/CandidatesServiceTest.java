package com.capgemini.security4;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.capgemini.security4.dto.CandidatesDto;
import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.entity.Party;
import com.capgemini.security4.entity.Users;
import com.capgemini.security4.exception.CandidateNotFound;
import com.capgemini.security4.exception.UserNotFoundException;
import com.capgemini.security4.repository.CandidatesRepository;
import com.capgemini.security4.repository.ElectionsRepository;
import com.capgemini.security4.repository.PartyRepository;
import com.capgemini.security4.repository.UserRepository;
import com.capgemini.security4.service.CandidatesServiceImpl;

@ExtendWith(MockitoExtension.class)
class CandidatesServiceTest {

    @Mock
    private CandidatesRepository candidatesRepository;

    @Mock
    private PartyRepository partyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ElectionsRepository electionsRepository;

    @InjectMocks
    private CandidatesServiceImpl candidatesService;

    @Test
    @DisplayName("Should return candidate when found by ID")
    void shouldReturnCandidateWhenFound() {
        Long candidateId = 1L;
        Candidates mockCandidate = new Candidates();
        mockCandidate.setCandidateId(candidateId);
        mockCandidate.setUserId(100L);
        mockCandidate.setElectionId(200L);
        mockCandidate.setManifesto("Test Manifesto");

        when(candidatesRepository.findById(candidateId))
            .thenReturn(Optional.of(mockCandidate));

        CandidatesDto result = candidatesService.getCandidatesById(candidateId);

        assertNotNull(result);
        assertEquals(candidateId, result.getCandidateId());
        assertEquals(100L, result.getUserId());
        assertEquals(200L, result.getElectionId());
        assertEquals("Test Manifesto", result.getManifesto());
    }

    @Test
    @DisplayName("Should throw exception when candidate not found")
    void shouldThrowExceptionWhenNotFound() {
        Long candidateId = 999L;
        when(candidatesRepository.findById(candidateId))
            .thenReturn(Optional.empty());

        assertThrows(CandidateNotFound.class, () -> {
            candidatesService.getCandidatesById(candidateId);
        });
    }

    @Test
    @DisplayName("Should create candidate successfully")
    void shouldCreateCandidateSuccessfully() {
        CandidatesDto dto = new CandidatesDto(null, 100L, 200L, 300L, "New Manifesto");
        Candidates savedCandidate = new Candidates();
        savedCandidate.setCandidateId(1L);
        savedCandidate.setUserId(100L);
        savedCandidate.setPartyId(200L);
        savedCandidate.setElectionId(300L);
        savedCandidate.setManifesto("New Manifesto");

        when(userRepository.findById(100L)).thenReturn(Optional.of(new Users()));
        when(electionsRepository.findById(300L)).thenReturn(Optional.of(new Elections()));
        when(partyRepository.findById(200L)).thenReturn(Optional.of(new Party()));
        when(candidatesRepository.save(any(Candidates.class))).thenReturn(savedCandidate);

        CandidatesDto result = candidatesService.createCandidates(dto);

        assertNotNull(result.getCandidateId());
        assertEquals(1L, result.getCandidateId());
        assertEquals(100L, result.getUserId());
        assertEquals(300L, result.getElectionId());
    }

    @Test
    @DisplayName("Should throw exception when creating with invalid user")
    void shouldThrowWhenCreatingWithInvalidUser() {
        CandidatesDto dto = new CandidatesDto(null, 999L, 200L, 300L, "Manifesto");
        
        when(userRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> {
            candidatesService.createCandidates(dto);
        });
    }
}