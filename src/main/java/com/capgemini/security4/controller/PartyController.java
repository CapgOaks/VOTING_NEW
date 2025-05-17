package com.capgemini.security4.controller;

import com.capgemini.security4.entity.Party;
import com.capgemini.security4.service.PartyService;

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

		Party createdParty = partyService.createParty(partyName, partyStatus, file);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdParty);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Party> getPartyById(@PathVariable Long id) {
		Party party = partyService.getPartyById(id);
		return ResponseEntity.ok(party);
	}

	@GetMapping
	public ResponseEntity<List<Party>> getAllParties() {
		List<Party> parties = partyService.getAllParties();
		return ResponseEntity.ok(parties);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Party> updateParty(@PathVariable Long id, @RequestBody Party updatedParty) {

		Party updated = partyService.updateParty(id, updatedParty);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteParty(@PathVariable Long id) {
		partyService.deleteParty(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/logo/{filename}")
	public ResponseEntity<Resource> getPartyLogo(@PathVariable String filename) throws java.io.IOException {
		Path filePath = Paths.get("uploads", filename);
		Resource resource = new UrlResource(filePath.toUri());

		if (resource.exists()) {
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG) 
					.body(resource);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
}
