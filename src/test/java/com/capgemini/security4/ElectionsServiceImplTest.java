package com.capgemini.security4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.exception.ElectionNotFoundException;
import com.capgemini.security4.repository.ElectionsRepository;
import com.capgemini.security4.service.ElectionsServiceImpl;

@DisplayName("Elections Service Impl Test")
public class ElectionsServiceImplTest {

	@Mock
	private ElectionsRepository electionsRepository;

	@InjectMocks
	private ElectionsServiceImpl electionsService;

	private Elections sampleElection;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		sampleElection = Elections.builder().electionId(1L).title("Test Election").description("Unit Test")
				.startDate(LocalDateTime.now()).endDate(LocalDateTime.now().plusDays(1)).electionStatus(true).build();
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

	        Elections updated = Elections.builder()
	                .title("Updated Title")
	                .description("Updated Description")
	                .startDate(LocalDateTime.now())
	                .endDate(LocalDateTime.now().plusDays(2))
	                .electionStatus(false)
	                .build();

	        Elections result = electionsService.updateElection(1L, updated);

	        assertNotNull(result);
	        verify(electionsRepository).save(any(Elections.class));
	    }

	    @Test
	    @DisplayName("Should throw exception when ID not found")
	    void testUpdateElection_NotFound() {
	        when(electionsRepository.findById(99L)).thenReturn(Optional.empty());

	        Elections updated = Elections.builder().title("Doesn't matter").build();

	        assertThrows(ElectionNotFoundException.class, () ->
	                electionsService.updateElection(99L, updated));
	    }
	}


	@Nested
	@DisplayName("Delete Election Tests")
	class DeleteTests {
		@Test
		@DisplayName("Should delete election by ID")
		void testDeleteElection() {
			when(electionsRepository.existsById(1L)).thenReturn(true);
			doNothing().when(electionsRepository).deleteById(1L);
			electionsService.deleteElection(1L);
			verify(electionsRepository, times(1)).deleteById(1L);
		}

		@Test
		@DisplayName("Should throw exception if election not found during delete")
		void testDeleteElection_NotFound() {
			when(electionsRepository.existsById(99L)).thenReturn(false);
			assertThrows(ElectionNotFoundException.class, () -> electionsService.deleteElection(99L));
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

}
