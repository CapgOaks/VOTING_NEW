package com.capgemini.security4.controller;

import com.capgemini.security4.entity.Party;
import com.capgemini.security4.service.PartyService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/parties")
@Slf4j
public class PartyController {

	private final PartyService partyService;

	@Autowired
	public PartyController(PartyService partyService) {
		this.partyService = partyService;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Party> createParty(@RequestParam("partyName") String partyName,
			@RequestParam("partyStatus") String partyStatus, @RequestParam("partyLogo") MultipartFile file)
			throws java.io.IOException {

		log.info("Received request to create party with name: {}, status: {}", partyName, partyStatus);
		Party createdParty = partyService.createParty(partyName, partyStatus, file);
		log.info("Party created successfully: {}", createdParty);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdParty);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Party> getPartyById(@PathVariable Long id) {
		log.info("Fetching party with ID: {}", id);
		Party party = partyService.getPartyById(id);
		log.info("Party retrieved: {}", party);
		return ResponseEntity.ok(party);
	}

	@GetMapping
	public ResponseEntity<List<Party>> getAllParties() {
		log.info("Fetching all parties");
		List<Party> parties = partyService.getAllParties();
		log.info("Total parties found: {}", parties.size());
		return ResponseEntity.ok(parties);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Party> updateParty(@PathVariable Long id, @RequestBody Party updatedParty) {

		log.info("Updating party with ID: {}, New details: {}", id, updatedParty);
		Party updated = partyService.updateParty(id, updatedParty);
		log.info("Party updated successfully: {}", updated);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteParty(@PathVariable Long id) {
		log.info("Deleting party with ID: {}", id);
		partyService.deleteParty(id);
		log.info("Party deleted successfully");
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/logo/{filename}")
	public ResponseEntity<Resource> getPartyLogo(@PathVariable String filename) throws java.io.IOException {
		Path filePath = Paths.get("uploads", filename);
		Resource resource = new UrlResource(filePath.toUri());

		if (resource.exists()) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
