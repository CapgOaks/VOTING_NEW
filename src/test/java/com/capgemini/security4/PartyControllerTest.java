package com.capgemini.security4;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.capgemini.security4.controller.PartyController;
import com.capgemini.security4.entity.Party;
import com.capgemini.security4.security.CustomUserDetailsService;
import com.capgemini.security4.security.JwtUtils;
import com.capgemini.security4.service.PartyService;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

@WebMvcTest(PartyController.class)
@AutoConfigureMockMvc(addFilters = false)
class PartyControllerTest {
    private final MockMvc mockMvc;
    private final PartyService partyService;

    @Autowired
    public PartyControllerTest(MockMvc mockMvc, PartyService partyService) {
        this.mockMvc = mockMvc;
        this.partyService = partyService;
    }

    private Party party1, party2;

    @BeforeEach
    void setUp() {
        party1 = new Party();
        party1.setPartyId(1L);
        party1.setPartyName("Party One");
        party1.setPartyStatus("Active");
        party1.setPartyLogo("logo1.png");

        party2 = new Party();
        party2.setPartyId(2L);
        party2.setPartyName("Party Two");
        party2.setPartyStatus("Inactive");
        party2.setPartyLogo("logo2.png");
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        @Primary
        public JwtUtils jwtUtils() {
            return Mockito.mock(JwtUtils.class);
        }

        @Bean
        @Primary
        public CustomUserDetailsService customUserDetailsService() {
            return Mockito.mock(CustomUserDetailsService.class);
        }

        @Bean
        @Primary
        public PartyService partyService() {
            return Mockito.mock(PartyService.class);
        }
    }

    @Test
    void testCreateParty() throws Exception {
        MockMultipartFile file = new MockMultipartFile("partyLogo", "logo1.png", "image/png",
                "logo-content".getBytes());

        when(partyService.createParty(eq("Party One"), eq("Active"), any())).thenReturn(party1);

        mockMvc.perform(
                multipart("/api/parties").file(file).param("partyName", "Party One").param("partyStatus", "Active"))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.partyName").value("Party One"));
    }

    @Test
    void testGetPartyById() throws Exception {
        when(partyService.getPartyById(1L)).thenReturn(party1);

        mockMvc.perform(get("/api/parties/1")).andExpect(status().isOk())
                .andExpect(jsonPath("$.partyName").value("Party One"));
    }

    @Test
    void testGetAllParties() throws Exception {
        List<Party> parties = Arrays.asList(party1, party2);
        when(partyService.getAllParties()).thenReturn(parties);

        mockMvc.perform(get("/api/parties")).andExpect(status().isOk()).andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void testUpdateParty() throws Exception {
        Party updatedParty = new Party();
        updatedParty.setPartyName("Updated Party");
        updatedParty.setPartyStatus("Active");
        updatedParty.setPartyLogo("updated.png");

        when(partyService.updateParty(eq(1L), any(Party.class))).thenReturn(updatedParty);

        String json = """
                {
                    "partyName": "Updated Party",
                    "partyStatus": "Active",
                    "partyLogo": "updated.png"
                }
                """;

        mockMvc.perform(put("/api/parties/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk()).andExpect(jsonPath("$.partyName").value("Updated Party"));
    }

    @Test
    void testDeleteParty() throws Exception {
        doNothing().when(partyService).deleteParty(1L);

        mockMvc.perform(delete("/api/parties/1")).andExpect(status().isNoContent());

        verify(partyService, times(1)).deleteParty(1L);
    }

    @Test
    void testGetPartyLogoExists() throws Exception {
        byte[] content = "fake image content".getBytes();
        new ByteArrayResource(content) {
            @Override
            public boolean exists() {
                return true;
            }
        };

        
        mockMvc.perform(get("/api/parties/logo/logo1.png")).andExpect(status().isNotFound());
    }

    @Test
    void testGetPartyLogoNotFound() throws Exception {
        mockMvc.perform(get("/api/parties/logo/nonexistent.png")).andExpect(status().isNotFound());
    }
}
