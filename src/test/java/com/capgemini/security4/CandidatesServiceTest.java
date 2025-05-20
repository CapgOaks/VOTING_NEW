package com.capgemini.security4;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.capgemini.security4.dto.CandidatesDto;
import com.capgemini.security4.dto.CandidateDto;
import com.capgemini.security4.dto.RunningCandidateDto;
import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.exception.CandidateNotFound;
import com.capgemini.security4.exception.ElectionNotFoundException;
import com.capgemini.security4.exception.PartyNotFoundException;
import com.capgemini.security4.exception.UserNotFoundException;
import com.capgemini.security4.repository.CandidatesRepository;
import com.capgemini.security4.repository.ElectionsRepository;
import com.capgemini.security4.repository.PartyRepository;
import com.capgemini.security4.repository.UserRepository;
import com.capgemini.security4.service.CandidatesServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class CandidatesServiceTest {

    @Mock
    private CandidatesRepository candidatesRepo;
    @Mock
    private PartyRepository partyRepo;
    @Mock
    private UserRepository userRepo;
    @Mock
    private ElectionsRepository electionRepo;

    @InjectMocks
    private CandidatesServiceImpl candidatesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCandidates() {
        Candidates candidate = new Candidates();
        when(candidatesRepo.findAll()).thenReturn(List.of(candidate));
        List<CandidatesDto> result = candidatesService.getAllCandidates();
        assertEquals(1, result.size());
    }

    @Test
    void testGetCandidatesById_Success() {
        Candidates candidate = new Candidates();
        candidate.setCandidateId(1L);
        when(candidatesRepo.findById(1L)).thenReturn(Optional.of(candidate));
        CandidatesDto dto = candidatesService.getCandidatesById(1L);
        assertNotNull(dto);
        assertEquals(1L, dto.getCandidateId());
    }

    @Test
    void testGetCandidatesById_NotFound() {
        when(candidatesRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CandidateNotFound.class, () -> candidatesService.getCandidatesById(99L));
    }

    @Test
    void testGetCandidateEntityById_Success() {
        Candidates candidate = new Candidates();
        when(candidatesRepo.findById(1L)).thenReturn(Optional.of(candidate));
        Candidates found = candidatesService.getCandidateEntityById(1L);
        assertNotNull(found);
    }

    @Test
    void testGetCandidateEntityById_NotFound() {
        when(candidatesRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CandidateNotFound.class, () -> candidatesService.getCandidateEntityById(99L));
    }

    @Test
    void testCreateCandidates_Success() {
        CandidatesDto dto = new CandidatesDto(null, 1L, 2L, 3L, "manifesto");
        when(userRepo.findById(1L)).thenReturn(Optional.of(new com.capgemini.security4.entity.Users()));
        when(electionRepo.findById(3L)).thenReturn(Optional.of(new com.capgemini.security4.entity.Elections()));
        when(partyRepo.findById(2L)).thenReturn(Optional.of(new com.capgemini.security4.entity.Party()));
        when(candidatesRepo.save(any())).thenReturn(new Candidates());
        CandidatesDto result = candidatesService.createCandidates(dto);
        assertNotNull(result);
    }

    @Test
    void testCreateCandidates_UserNotFound() {
        CandidatesDto dto = new CandidatesDto(null, 1L, 2L, 3L, "manifesto");
        when(userRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> candidatesService.createCandidates(dto));
    }

    @Test
    void testCreateCandidates_ElectionNotFound() {
        CandidatesDto dto = new CandidatesDto(null, 1L, 2L, 3L, "manifesto");
        when(userRepo.findById(1L)).thenReturn(Optional.of(new com.capgemini.security4.entity.Users()));
        when(electionRepo.findById(3L)).thenReturn(Optional.empty());
        assertThrows(ElectionNotFoundException.class, () -> candidatesService.createCandidates(dto));
    }

    @Test
    void testCreateCandidates_PartyNotFound() {
        CandidatesDto dto = new CandidatesDto(null, 1L, 2L, 3L, "manifesto");
        when(userRepo.findById(1L)).thenReturn(Optional.of(new com.capgemini.security4.entity.Users()));
        when(electionRepo.findById(3L)).thenReturn(Optional.of(new com.capgemini.security4.entity.Elections()));
        when(partyRepo.findById(2L)).thenReturn(Optional.empty());
        assertThrows(PartyNotFoundException.class, () -> candidatesService.createCandidates(dto));
    }

    @Test
    void testUpdateCandidates_Success() {
        CandidatesDto dto = new CandidatesDto(null, 1L, 2L, 3L, "updated");
        Candidates existing = new Candidates();
        when(candidatesRepo.findById(1L)).thenReturn(Optional.of(existing));
        when(candidatesRepo.save(any())).thenReturn(existing);
        CandidatesDto result = candidatesService.updateCandidates(1L, dto);
        assertNotNull(result);
    }

    @Test
    void testUpdateCandidates_NotFound() {
        CandidatesDto dto = new CandidatesDto(null, 1L, 2L, 3L, "updated");
        when(candidatesRepo.findById(99L)).thenReturn(Optional.empty());
        assertThrows(CandidateNotFound.class, () -> candidatesService.updateCandidates(99L, dto));
    }

    @Test
    void testDeleteCandidates_Success() {
        when(candidatesRepo.existsById(1L)).thenReturn(true);
        doNothing().when(candidatesRepo).deleteById(1L);
        candidatesService.deleteCandidates(1L);
        verify(candidatesRepo).deleteById(1L);
    }

    @Test
    void testDeleteCandidates_NotFound() {
        when(candidatesRepo.existsById(99L)).thenReturn(false);
        assertThrows(CandidateNotFound.class, () -> candidatesService.deleteCandidates(99L));
    }

    @Test
    void testGetCandidatesByElectionId() {
        CandidateDto candidateDto = mock(CandidateDto.class);
        when(candidatesRepo.findCandidateDtosByElectionId(1L)).thenReturn(List.of(candidateDto));
        List<CandidateDto> result = candidatesService.getCandidatesByElectionId(1L);
        assertEquals(1, result.size());
    }

    @Test
    void testGetRunningCandidates() {
        RunningCandidateDto runningCandidateDto = mock(RunningCandidateDto.class);
        when(candidatesRepo.findRunningCandidates("active")).thenReturn(List.of(runningCandidateDto));
        List<RunningCandidateDto> result = candidatesService.getRunningCandidates();
        assertEquals(1, result.size());
    }
}