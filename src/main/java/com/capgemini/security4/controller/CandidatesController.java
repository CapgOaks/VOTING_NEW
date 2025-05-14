package com.capgemini.security4.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.service.CandidatesService;



@RestController
@RequestMapping("/api/candidates")
public class CandidatesController {
	private CandidatesService candidatesService;
	@Autowired
	public CandidatesController(CandidatesService candidatesService) {
		super();
		this.candidatesService = candidatesService;
	}
	@GetMapping
	public ResponseEntity<List<Candidates>> getAllCandidates() {
		List<Candidates> candidates = candidatesService.getAllCandidates();
		return ResponseEntity.status(HttpStatus.OK).body(candidates);
	}
	@PostMapping
	public ResponseEntity<Candidates> createCandidates(@RequestBody Candidates  candidates) {
		Candidates saved = candidatesService.createCandidates(candidates);
		return ResponseEntity.status(HttpStatus.CREATED).body(saved);
	}
	@PutMapping("/{id}")
	public ResponseEntity<Candidates> updateCandidates(@PathVariable Long id,@RequestBody Candidates newCandidates) {
		return ResponseEntity.status(HttpStatus.OK).body(candidatesService.updateCandidates(id, newCandidates));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCandidates(@PathVariable Long id) {
		candidatesService.deleteCandidates(id);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	
}
