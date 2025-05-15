package com.capgemini.security4.controller;

import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.service.VotesService;

import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@Slf4j
public class VotesController {

	private VotesService votesService;

	@Autowired
	public VotesController(VotesService votesService) {
		this.votesService = votesService;
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<Boolean> checkUserVoteStatus(@PathVariable Long userId) {
		log.info("Checking vote status for user with ID: {}", userId);

		boolean hasVoted = votesService.hasUserVoted(userId);

		log.info("User with ID: {} has voted: {}", userId, hasVoted);
		return ResponseEntity.ok(hasVoted);
	}

	@PostMapping
  public ResponseEntity<Votes> castVote(@Valid @RequestBody Votes vote, BindingResult bindingResult) {

    if (bindingResult.hasErrors()) {
        throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
    }

    log.info("Received vote submission: {}", vote);

    Votes savedVote = votesService.castVote(vote);

    log.info("Vote successfully saved: {}", savedVote);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedVote);
  }

}
