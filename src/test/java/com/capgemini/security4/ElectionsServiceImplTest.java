package com.capgemini.security4;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.capgemini.security4.dto.VotersCountDto;
import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.exception.ElectionAlreadyExistException;
import com.capgemini.security4.exception.ElectionNotFoundException;
import com.capgemini.security4.repository.ElectionsRepository;
import com.capgemini.security4.repository.VotesRepository;
import com.capgemini.security4.service.ElectionsServiceImpl;
import com.capgemini.security4.repository.VotesRepository.VotersCountProjection;
@DisplayName("Elections Service Impl Test")
class ElectionsServiceImplTest {

	@Mock
	private ElectionsRepository electionsRepository;

	@InjectMocks
	private ElectionsServiceImpl electionsService;
	

    @Mock
    private VotesRepository votesRepository;


	private Elections sampleElection;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sampleElection = new Elections();
		sampleElection.setElectionId(1L);
		sampleElection.setTitle("Test Election");
		sampleElection.setDescription("Unit Test");
		sampleElection.setStartDate(LocalDateTime.now());
		sampleElection.setEndDate(LocalDateTime.now().plusDays(1));
		sampleElection.setElectionStatus(true);
	}

	@Nested
	@DisplayName("Create Election Tests")
	class CreateElectionTests {
		@Test
		@DisplayName("Should save and return the created election")
		void testCreateElection() {
			when(electionsRepository.save(sampleElection)).thenReturn(sampleElection);
			Elections result = electionsService.createElection(sampleElection);
			assertEquals("Test Election", result.getTitle());
		}
	}

	@Nested
	@DisplayName("Get Election By ID Tests")
	class GetByIdTests {

		@Test
		@DisplayName("Should return election when ID exists")
		void testGetElectionById_Success() {
			when(electionsRepository.findById(1L)).thenReturn(Optional.of(sampleElection));
			Elections found = electionsService.getElectionById(1L);
			assertNotNull(found);
			assertEquals(1L, found.getElectionId());
		}

		@Test
		@DisplayName("Should throw exception when election not found")
		void testGetElectionById_NotFound() {
			when(electionsRepository.findById(99L)).thenReturn(Optional.empty());
			assertThrows(ElectionNotFoundException.class, () -> electionsService.getElectionById(99L));
		}
	}

	@Nested
	@DisplayName("Get All Elections Tests")
	class GetAllTests {
		@Test
		@DisplayName("Should return all elections")
		void testGetAllElections() {
			when(electionsRepository.findAll()).thenReturn(List.of(sampleElection));
			List<Elections> result = electionsService.getAllElections();
			assertEquals(1, result.size());
		}
	}

	@Nested
	@DisplayName("Update Election Tests")
	class UpdateTests {

		@Test
		@DisplayName("Should update election when ID exists")
		void testUpdateElection_Success() {
			when(electionsRepository.findById(1L)).thenReturn(Optional.of(sampleElection));
			when(electionsRepository.save(any(Elections.class))).thenReturn(sampleElection);

			Elections updated = new Elections();
			updated.setTitle("Updated Title");
			updated.setDescription("Updated Description");
			updated.setStartDate(LocalDateTime.now());
			updated.setEndDate(LocalDateTime.now().plusDays(2));
			updated.setElectionStatus(false);

			Elections result = electionsService.updateElection(1L, updated);

			assertNotNull(result);
			verify(electionsRepository).save(any(Elections.class));
		}

		@Test
		@DisplayName("Should throw exception when ID not found")
		void testUpdateElection_NotFound() {
			when(electionsRepository.findById(99L)).thenReturn(Optional.empty());

			Elections updated = new Elections();
			updated.setTitle("Doesn't matter");

			assertThrows(ElectionNotFoundException.class, () -> electionsService.updateElection(99L, updated));
		}
	}

	@Nested
	@DisplayName("Status Filter Tests")
	class StatusTests {
		@Test
		@DisplayName("Should return elections with true status")
		void testGetByStatusTrue() {
			when(electionsRepository.findByElectionStatus(true)).thenReturn(List.of(sampleElection));
			List<Elections> result = electionsService.getElectionsByStatus(true);
			assertEquals(1, result.size());
		}

		@Test
		@DisplayName("Should return empty list when no elections match status")
		void testGetByStatusEmpty() {
			when(electionsRepository.findByElectionStatus(false)).thenReturn(List.of());
			List<Elections> result = electionsService.getElectionsByStatus(false);
			assertTrue(result.isEmpty());
		}
	}
	@Test
	void testCreateElection_Success() {
	    Elections election = new Elections();
	    election.setTitle("Election 1");
	    when(electionsRepository.existsByTitle("Election 1")).thenReturn(false);
	    when(electionsRepository.save(election)).thenReturn(election);
	    Elections result = electionsService.createElection(election);
	    assertNotNull(result);
	}

	@Test
	void testCreateElection_AlreadyExists() {
	    Elections election = new Elections();
	    election.setTitle("Election 1");
	    when(electionsRepository.existsByTitle("Election 1")).thenReturn(true);
	    assertThrows(ElectionAlreadyExistException.class, () -> electionsService.createElection(election));
	}

	@Test
	void testGetElectionById_Success() {
	    Elections election = new Elections();
	    election.setElectionId(1L);
	    when(electionsRepository.findById(1L)).thenReturn(java.util.Optional.of(election));
	    Elections found = electionsService.getElectionById(1L);
	    assertNotNull(found);
	}

	@Test
	void testGetElectionById_NotFound() {
	    when(electionsRepository.findById(99L)).thenReturn(java.util.Optional.empty());
	    assertThrows(ElectionNotFoundException.class, () -> electionsService.getElectionById(99L));
	}

	@Test
	void testGetAllElections() {
	    List<Elections> elections = List.of(new Elections());
	    when(electionsRepository.findAll()).thenReturn(elections);
	    List<Elections> result = electionsService.getAllElections();
	    assertFalse(result.isEmpty());
	}

	@Test
	void testUpdateElection_Success() {
	    Elections existing = new Elections();
	    existing.setElectionId(1L);
	    Elections updated = new Elections();
	    updated.setTitle("Updated");
	    updated.setDescription("Desc");
	    updated.setStartDate(existing.getStartDate());
	    updated.setEndDate(existing.getEndDate());
	    updated.setElectionStatus(true);
	    when(electionsRepository.findById(1L)).thenReturn(java.util.Optional.of(existing));
	    when(electionsRepository.save(any())).thenReturn(existing);
	    Elections result = electionsService.updateElection(1L, updated);
	    assertNotNull(result);
	}

	@Test
	void testUpdateElection_NotFound() {
	    Elections updated = new Elections();
	    when(electionsRepository.findById(99L)).thenReturn(java.util.Optional.empty());
	    assertThrows(ElectionNotFoundException.class, () -> electionsService.updateElection(99L, updated));
	}

	@Test
	void testGetElectionsByStatus() {
	    List<Elections> elections = List.of(new Elections());
	    when(electionsRepository.findByElectionStatus(true)).thenReturn(elections);
	    List<Elections> result = electionsService.getElectionsByStatus(true);
	    assertFalse(result.isEmpty());
	}

	@Test
	void testGetUpcomingElections() {
	    List<Elections> elections = List.of(new Elections());
	    when(electionsRepository.findUpcomingElections()).thenReturn(elections);
	    List<Elections> result = electionsService.getUpcomingElections();
	    assertFalse(result.isEmpty());
	}

	@Test
	void testGetVotersCountPerElection() {
	    VotersCountProjection projection = mock(VotersCountProjection.class);
	    when(projection.getElectionId()).thenReturn(1L);
	    when(projection.getElectionName()).thenReturn("Election 1");
	    when(projection.getVotersCount()).thenReturn(100L);
	    when(votesRepository.findVotersCountPerElection()).thenReturn(List.of(projection));

	    List<VotersCountDto> result = electionsService.getVotersCountPerElection();
	    assertEquals(1, result.size());
	    assertEquals("Election 1", result.get(0).getElectionName());
	}

	@Test
	void testExistsByTitle() {
	    when(electionsRepository.existsByTitle("Election 1")).thenReturn(true);
	    assertTrue(electionsService.existsByTitle("Election 1"));
	}

}
