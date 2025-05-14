package com.capgemini.security4.service;

import com.capgemini.security4.entity.Party;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface PartyService {
	Party createParty(String partyName, String partyStatus, MultipartFile file) throws IOException;

	Party getPartyById(Long id);

	List<Party> getAllParties();

	Party updateParty(Long id, Party updatedParty);

	void deleteParty(Long id);
}
