package com.capgemini.security4.service;

import com.capgemini.security4.entity.Party;
import com.capgemini.security4.exception.PartyAlreadyExistsException;
import com.capgemini.security4.exception.PartyNotFoundException;
import com.capgemini.security4.repository.PartyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PartyServiceImpl implements PartyService {

	private final PartyRepository partyRepository;
	private final String UPLOAD_DIR = "uploads/";

	@Autowired
	public PartyServiceImpl(PartyRepository partyRepository) {
		this.partyRepository = partyRepository;
	}

	@Override
	public Party createParty(String partyName, String partyStatus, MultipartFile file) throws IOException {
		Optional<Party> existing = partyRepository.findByPartyName(partyName);
		if (existing.isPresent()) {
			throw new PartyAlreadyExistsException("Party with name '" + partyName + "' already exists.");
		}

		Files.createDirectories(Paths.get(UPLOAD_DIR));

		
		String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
		Path filePath = Paths.get(UPLOAD_DIR, fileName);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		
		Party party = new Party();
		party.setPartyName(partyName);
		party.setPartyStatus(partyStatus);
		party.setPartyLogo(fileName);

		return partyRepository.save(party);
	}

	@Override
	public Party getPartyById(Long id) {
		return partyRepository.findById(id)
				.orElseThrow(() -> new PartyNotFoundException("Party with ID " + id + " not found."));
	}

	@Override
	public List<Party> getAllParties() {
		return partyRepository.findAll();
	}

	@Override
	public Party updateParty(Long id, Party updatedParty) {
		Party existing = getPartyById(id);
		existing.setPartyName(updatedParty.getPartyName());
		existing.setPartyStatus(updatedParty.getPartyStatus());
		existing.setPartyLogo(updatedParty.getPartyLogo());
		return partyRepository.save(existing);
	}

	@Override
	public void deleteParty(Long id) {
		Party party = getPartyById(id);
		partyRepository.delete(party);
	}
}
