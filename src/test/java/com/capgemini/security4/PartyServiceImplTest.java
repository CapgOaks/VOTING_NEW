package com.capgemini.security4;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.capgemini.security4.entity.Party;
import com.capgemini.security4.exception.PartyAlreadyExistsException;
import com.capgemini.security4.exception.PartyNotFoundException;
import com.capgemini.security4.repository.PartyRepository;
import com.capgemini.security4.service.PartyServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;

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
        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("logo.png");
        when(file.getInputStream()).thenReturn(mock(InputStream.class));
        when(partyRepository.findByPartyName("TestParty")).thenReturn(Optional.empty());
        when(partyRepository.save(any())).thenReturn(new Party());

        // Mock static Files methods
        try (MockedStatic<Files> filesMock = mockStatic(Files.class)) {
            filesMock.when(() -> Files.createDirectories(any(Path.class))).thenReturn(null);
            filesMock.when(() -> Files.copy(any(InputStream.class), any(Path.class), any(StandardCopyOption.class))).thenReturn(1L);

            Party result = partyService.createParty("TestParty", "Active", file);
            assertNotNull(result);
        }
    }

    @Test
    void testCreateParty_AlreadyExists() {
        MultipartFile file = mock(MultipartFile.class);
        when(partyRepository.findByPartyName("TestParty")).thenReturn(Optional.of(new Party()));
        assertThrows(PartyAlreadyExistsException.class, () -> partyService.createParty("TestParty", "Active", file));
    }

    @Test
    void testGetPartyById_Success() {
        Party party = new Party();
        party.setPartyName("TestParty");
        when(partyRepository.findById(1L)).thenReturn(Optional.of(party));
        Party found = partyService.getPartyById(1L);
        assertNotNull(found);
        assertEquals("TestParty", found.getPartyName());
    }

    @Test
    void testGetPartyById_NotFound() {
        when(partyRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(PartyNotFoundException.class, () -> partyService.getPartyById(99L));
    }

    @Test
    void testGetAllParties() {
        when(partyRepository.findAll()).thenReturn(List.of(new Party()));
        List<Party> result = partyService.getAllParties();
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateParty_Success() {
        Party existing = new Party();
        existing.setPartyName("OldName");
        Party updated = new Party();
        updated.setPartyName("NewName");
        updated.setPartyStatus("Active");
        when(partyRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(partyRepository.save(any())).thenReturn(existing);
        Party result = partyService.updateParty(1L, updated);
        assertNotNull(result);
        assertEquals("NewName", existing.getPartyName());
    }

    @Test
    void testUpdateParty_NotFound() {
        Party updated = new Party();
        when(partyRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(PartyNotFoundException.class, () -> partyService.updateParty(99L, updated));
    }

    @Test
    void testDeleteParty_Success() {
        Party party = new Party();
        when(partyRepository.findById(1L)).thenReturn(Optional.of(party));
        doNothing().when(partyRepository).delete(party);
        partyService.deleteParty(1L);
        verify(partyRepository).delete(party);
    }

    @Test
    void testDeleteParty_NotFound() {
        when(partyRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(PartyNotFoundException.class, () -> partyService.deleteParty(99L));
    }
}