package com.capgemini.security4;

import com.capgemini.security4.entity.Party;
import com.capgemini.security4.service.PartyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PartyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PartyService partyService;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(partyService);
    }

    @Test
    void testCreateParty_Success() throws Exception {
        Party party = new Party(1L, "TestParty", "Active", "logo.png");
        Mockito.when(partyService.createParty(anyString(), anyString(), any())).thenReturn(party);

        MockMultipartFile file = new MockMultipartFile("partyLogo", "logo.png", "image/png", "dummy".getBytes());

        mockMvc.perform(multipart("/api/parties")
                .file(file)
                .param("partyName", "TestParty")
                .param("partyStatus", "Active"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.partyName").value("TestParty"));
    }

    @Test
    void testGetPartyById() throws Exception {
        Party party = new Party(1L, "TestParty", "Active", "logo.png");
        Mockito.when(partyService.getPartyById(1L)).thenReturn(party);

        mockMvc.perform(get("/api/parties/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyName").value("TestParty"));
    }

    @Test
    void testGetAllParties() throws Exception {
        Party party = new Party(1L, "TestParty", "Active", "logo.png");
        Mockito.when(partyService.getAllParties()).thenReturn(List.of(party));

        mockMvc.perform(get("/api/parties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].partyName").value("TestParty"));
    }

    @Test
    void testUpdateParty() throws Exception {
        Party updated = new Party(1L, "UpdatedParty", "Inactive", "logo2.png");
        Mockito.when(partyService.updateParty(eq(1L), any(Party.class))).thenReturn(updated);

        String json = """
            {
                "partyId": 1,
                "partyName": "UpdatedParty",
                "partyStatus": "Inactive",
                "partyLogo": "logo2.png"
            }
        """;

        mockMvc.perform(put("/api/parties/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partyName").value("UpdatedParty"));
    }

    @Test
    void testDeleteParty() throws Exception {
        Mockito.doNothing().when(partyService).deleteParty(1L);

        mockMvc.perform(delete("/api/parties/1"))
                .andExpect(status().isNoContent());
    }



    @Test
    void testGetPartyLogo_NotFound() throws Exception {
        mockMvc.perform(get("/api/parties/logo/nonexistent.png"))
                .andExpect(status().isNotFound());
    }

    @TestConfiguration
    static class PartyControllerTestConfig {
        @Bean
        @Primary
        public PartyService partyService() {
            return Mockito.mock(PartyService.class);
        }
    }
}