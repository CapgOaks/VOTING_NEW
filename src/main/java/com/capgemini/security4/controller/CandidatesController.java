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
import com.capgemini.security4.entity.Candidates;
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

    // GET all candidates
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

    // POST create
    @PostMapping
    public ResponseEntity<CandidatesDto> createCandidate(
        @Valid @RequestBody CandidatesDto dto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Validation errors: " + bindingResult.getAllErrors());
        }
        CandidatesDto created = candidatesService.createCandidates(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // PUT update
    @PutMapping("/{id}")
    public ResponseEntity<CandidatesDto> updateCandidate(
        @PathVariable Long id,
        @Valid @RequestBody CandidatesDto dto,
        BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("Validation errors: " + bindingResult.getAllErrors());
        }
        CandidatesDto updated = candidatesService.updateCandidates(id, dto);
        return ResponseEntity.ok(updated);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidatesService.deleteCandidates(id);
        return ResponseEntity.noContent().build();
    }
}