package com.capgemini.security4;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.capgemini.security4.entity.*;
import com.capgemini.security4.repository.VotesRepository;
import com.capgemini.security4.service.VotesServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class VotesServiceImplTest {

    @Mock
    private VotesRepository votesRepository;

    @InjectMocks
    private VotesServiceImpl votesService;

    private Users testUser;
    private Candidates testCandidate;
    private Elections testElection;
    private Votes testVote;

    @BeforeEach
    void setUp() {
        testUser = new Users();
        testUser.setUserId(1L);
        
        testCandidate = new Candidates();
        testCandidate.setCandidateId(1L);
        
        testElection = new Elections();
        testElection.setElectionId(1L);
        
        testVote = new Votes();
        testVote.setUser(testUser);
        testVote.setCandidate(testCandidate);
        testVote.setElection(testElection);
    }

    @Test
    @DisplayName("hasUserVoted - should return true when user has voted")
    void hasUserVoted_ShouldReturnTrue_WhenUserHasVoted() {
        // Arrange
        when(votesRepository.existsByUser_UserId(1L)).thenReturn(true);

        // Act
        boolean result = votesService.hasUserVoted(1L);

        // Assert
        assertTrue(result);
        verify(votesRepository).existsByUser_UserId(1L);
    }

    @Test
    @DisplayName("hasUserVoted - should return false when user hasn't voted")
    void hasUserVoted_ShouldReturnFalse_WhenUserHasNotVoted() {
        // Arrange
        when(votesRepository.existsByUser_UserId(1L)).thenReturn(false);

        // Act
        boolean result = votesService.hasUserVoted(1L);

        // Assert
        assertFalse(result);
        verify(votesRepository).existsByUser_UserId(1L);
    }

    @Test
    @DisplayName("castVote - should successfully cast valid vote")
    void castVote_ShouldSaveVote_WhenValid() {
        // Arrange
        when(votesRepository.existsByUser_UserId(1L)).thenReturn(false);
        when(votesRepository.save(testVote)).thenReturn(testVote);

        // Act
        Votes result = votesService.castVote(testVote);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getTimeStamp());
        verify(votesRepository).save(testVote);
    }

    @Test
    @DisplayName("castVote - should throw BAD_REQUEST when missing user")
    void castVote_ShouldThrowBadRequest_WhenMissingUser() {
        // Arrange
        testVote.setUser(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> votesService.castVote(testVote));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User, candidate, and election must be specified", exception.getReason());
        verify(votesRepository, never()).save(any());
    }

    @Test
    @DisplayName("castVote - should throw BAD_REQUEST when missing candidate")
    void castVote_ShouldThrowBadRequest_WhenMissingCandidate() {
        // Arrange
        testVote.setCandidate(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> votesService.castVote(testVote));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User, candidate, and election must be specified", exception.getReason());
        verify(votesRepository, never()).save(any());
    }

    @Test
    @DisplayName("castVote - should throw BAD_REQUEST when missing election")
    void castVote_ShouldThrowBadRequest_WhenMissingElection() {
        // Arrange
        testVote.setElection(null);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> votesService.castVote(testVote));
        
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("User, candidate, and election must be specified", exception.getReason());
        verify(votesRepository, never()).save(any());
    }

    @Test
    @DisplayName("castVote - should throw CONFLICT when user already voted")
    void castVote_ShouldThrowConflict_WhenUserAlreadyVoted() {
        // Arrange
        when(votesRepository.existsByUser_UserId(1L)).thenReturn(true);

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
            () -> votesService.castVote(testVote));
        
        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        assertEquals("User has already voted", exception.getReason());
        verify(votesRepository, never()).save(any());
    }

    @Test
    @DisplayName("castVote - should set timestamp before saving")
    void castVote_ShouldSetTimestamp_BeforeSaving() {
        // Arrange
        when(votesRepository.existsByUser_UserId(1L)).thenReturn(false);
        when(votesRepository.save(testVote)).thenAnswer(invocation -> {
            Votes vote = invocation.getArgument(0);
            assertNotNull(vote.getTimeStamp());
            return vote;
        });

        // Act
        Votes result = votesService.castVote(testVote);

        // Assert
        assertNotNull(result);
        verify(votesRepository).save(testVote);
    }
}
