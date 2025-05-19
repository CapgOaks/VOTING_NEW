package com.capgemini.security4;

import com.capgemini.security4.entity.Party;
import com.capgemini.security4.exception.PartyAlreadyExistsException;
import com.capgemini.security4.exception.PartyNotFoundException;
import com.capgemini.security4.repository.PartyRepository;
import com.capgemini.security4.service.PartyServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PartyServiceImplTest {

	@Mock
	private PartyRepository partyRepository;

	@InjectMocks
	private PartyServiceImpl partyService;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testCreateParty_Success() throws IOException {
		String partyName = "Test Party";
		String partyStatus = "Active";
		MockMultipartFile file = new MockMultipartFile("logo", "logo.png", "image/png", "image".getBytes());

		when(partyRepository.findByPartyName(partyName)).thenReturn(Optional.empty());
		when(partyRepository.save(any(Party.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Party result = partyService.createParty(partyName, partyStatus, file);

		assertEquals(partyName, result.getPartyName());
		assertEquals(partyStatus, result.getPartyStatus());
		assertNotNull(result.getPartyLogo());
	}

	@Test
	void testCreateParty_AlreadyExists() {
		String partyName = "Existing Party";
		MockMultipartFile file = new MockMultipartFile("logo", "logo.png", "image/png", "image".getBytes());

		when(partyRepository.findByPartyName(partyName)).thenReturn(Optional.of(new Party()));

		assertThrows(PartyAlreadyExistsException.class, () -> partyService.createParty(partyName, "Active", file));
		verify(partyRepository, never()).save(any());
	}

	@Test
	void testGetPartyById_Success() {
		Party party = new Party();
		party.setPartyId(1L);
		party.setPartyName("Test");

		when(partyRepository.findById(1L)).thenReturn(Optional.of(party));

		Party result = partyService.getPartyById(1L);

		assertEquals("Test", result.getPartyName());
	}

	@Test
	void testGetPartyById_NotFound() {
		when(partyRepository.findById(2L)).thenReturn(Optional.empty());

		assertThrows(PartyNotFoundException.class, () -> partyService.getPartyById(2L));
	}

	@Test
	void testGetAllParties() {
		List<Party> parties = Arrays.asList(new Party(), new Party());
		when(partyRepository.findAll()).thenReturn(parties);

		List<Party> result = partyService.getAllParties();
		assertEquals(2, result.size());
	}

	@Test
	void testUpdateParty() {
		Party existing = new Party();
		existing.setPartyId(1L);
		existing.setPartyName("Old");
		existing.setPartyStatus("Inactive");
		

		Party updated = new Party();
		updated.setPartyName("New");
		updated.setPartyStatus("Active");
		

		when(partyRepository.findById(1L)).thenReturn(Optional.of(existing));
		when(partyRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		Party result = partyService.updateParty(1L, updated);

		assertEquals("New", result.getPartyName());
		assertEquals("Active", result.getPartyStatus());
	}

	@Test
	void testDeleteParty() {
		Party party = new Party();
		party.setPartyId(1L);

		when(partyRepository.findById(1L)).thenReturn(Optional.of(party));

		partyService.deleteParty(1L);

		verify(partyRepository).delete(party);
	}
}
