package com.capgemini.security4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.dto.CandidatesDto;
import com.capgemini.security4.dto.CandidateDto;
import com.capgemini.security4.service.CandidatesService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
@RestController
@RequestMapping("/api/candidates")
@Slf4j
public class CandidatesController {
    private final CandidatesService candidatesService;

    @Autowired
    public CandidatesController(CandidatesService candidatesService) {
        this.candidatesService = candidatesService;
    }

    @GetMapping
    public ResponseEntity<List<CandidatesDto>> getAllCandidates() {
        List<CandidatesDto> dtos = candidatesService.getAllCandidates();
        return ResponseEntity.ok(dtos);
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<CandidatesDto> getCandidateById(@PathVariable Long id) {
        CandidatesDto dto = candidatesService.getCandidatesById(id);
        return ResponseEntity.ok(dto);
    }

	@GetMapping("/election/{electionId}")
	public ResponseEntity<List<CandidateDto>> getByElection(@PathVariable Long electionId) {
		List<CandidateDto> dtos = candidatesService.getCandidatesByElectionId(electionId);
		return ResponseEntity.ok(dtos);
	}

	@PostMapping
	public ResponseEntity<CandidatesDto> createCandidates(@Valid @RequestBody CandidatesDto candidates,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		log.info("Creating new candidate: {}", candidates);
		CandidatesDto saved = candidatesService.createCandidates(candidates);
		log.info("Candidate created successfully: {}", saved);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CandidatesDto> updateCandidates(@PathVariable Long id,
			@Valid @RequestBody CandidatesDto newCandidates, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}
		log.info("Updating candidate with ID: {}. New data: {}", id, newCandidates);
		CandidatesDto updated = candidatesService.updateCandidates(id, newCandidates);
		log.info("Candidate updated successfully: {}", updated);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCandidates(@PathVariable Long id) {
		log.info("Deleting candidate with ID: {}", id);
		candidatesService.deleteCandidates(id);
		log.info("Candidate deleted successfully");
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@GetMapping("/running")
	public ResponseEntity<List<com.capgemini.security4.dto.RunningCandidateDto>> getRunningCandidates() {
		log.info("Fetching running candidates");
		List<com.capgemini.security4.dto.RunningCandidateDto> runningCandidates = candidatesService.getRunningCandidates();
		log.info("Found {} running candidates", runningCandidates.size());
		return ResponseEntity.status(HttpStatus.OK).body(runningCandidates);
	}
}
