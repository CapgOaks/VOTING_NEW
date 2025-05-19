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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.service.ElectionsService;
import com.capgemini.security4.dto.VotersCountDto;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/elections")
@Slf4j
public class ElectionsController {

	private ElectionsService electionsService;

	@Autowired
	public ElectionsController(ElectionsService electionsService) {
		super();
		this.electionsService = electionsService;
	}

	@GetMapping
	public ResponseEntity<List<Elections>> getAllElections() {
		log.info("Fetching all elections");
		List<Elections> elections = electionsService.getAllElections();
		log.info("Found {} elections", elections.size());
		return ResponseEntity.status(HttpStatus.OK).body(elections);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Elections> getElectionById(@PathVariable Long id) {
		log.info("Fetching election with ID: {}", id);
		Elections election = electionsService.getElectionById(id);
		log.info("Election retrieved: {}", election);
		return ResponseEntity.status(HttpStatus.OK).body(election);
	}

	@GetMapping("/status")
	public ResponseEntity<List<Elections>> getElectionsByStatus(@RequestParam Boolean status) {
		log.info("Fetching elections with status: {}", status);
		List<Elections> elections = electionsService.getElectionsByStatus(status);
		log.info("Found {} elections with status {}", elections.size(), status);
		return ResponseEntity.status(HttpStatus.OK).body(elections);
	}

	@GetMapping("/upcoming")
	public ResponseEntity<List<Elections>> getUpcomingElections() {
		log.info("Fetching upcoming elections");
		List<Elections> elections = electionsService.getUpcomingElections();
		log.info("Found {} upcoming elections", elections.size());
		return ResponseEntity.status(HttpStatus.OK).body(elections);
	}

	@GetMapping("/voters-count")
	public ResponseEntity<List<VotersCountDto>> getVotersCountPerElection() {
		log.info("Fetching voters count per election");
		List<VotersCountDto> votersCount = electionsService.getVotersCountPerElection();
		log.info("Found voters count data for {} elections", votersCount.size());
		return ResponseEntity.status(HttpStatus.OK).body(votersCount);
	}

	@PostMapping
	public ResponseEntity<Elections> createElection(@Valid @RequestBody Elections election,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}

		log.info("Creating new election: {}", election);
		Elections created = electionsService.createElection(election);
		log.info("Election created successfully: {}", created);
		return ResponseEntity.status(HttpStatus.CREATED).body(created);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Elections> updateElection(@PathVariable Long id,
			@Valid @RequestBody Elections updatedElection, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}

		log.info("Updating election with ID: {}. New data: {}", id, updatedElection);
		Elections updated = electionsService.updateElection(id, updatedElection);
		log.info("Election updated successfully: {}", updated);
		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteElection(@PathVariable Long id) {
		log.info("Deleting election with ID: {}", id);
		electionsService.deleteElection(id);
		log.info("Election deleted successfully");
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
