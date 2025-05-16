package com.capgemini.security4.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.entity.Users;
import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.service.UserService;
import com.capgemini.security4.service.VotesService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/votes")
@Slf4j
public class VotesController {

	private VotesService votesService;
	private UserService userService;

	@Autowired
	public VotesController(VotesService votesService, UserService userService) {
		super();
		this.votesService = votesService;
		this.userService = userService;
	}
	

	@GetMapping("/user/{userId}")
	public ResponseEntity<Boolean> checkUserVoteStatus(@PathVariable Long userId) {
		log.info("Checking vote status for user with ID: {}", userId);

		boolean hasVoted = votesService.hasUserVoted(userId);

		log.info("User with ID: {} has voted: {}", userId, hasVoted);
		return ResponseEntity.ok(hasVoted);
	}


	@PostMapping("/user/{userId}")
	public ResponseEntity<Map<String, Object>> castVote(@PathVariable Long userId,
	    @Valid @RequestBody Votes vote, BindingResult bindingResult) {

	    if (bindingResult.hasErrors()) {
	        throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
	    }

	    Users user = userService.findByUserId(userId);
	    if (user == null) {
	        throw new IllegalArgumentException("User not found with id: " + userId);
	    }

	    vote.setUser(user);


	    vote.setTimeStamp(LocalDateTime.now());

	    Votes savedVote = votesService.castVote(vote);

	    Map<String, Object> response = new HashMap<>();
	    response.put("message", "Vote successfully cast");
	    response.put("voteId", savedVote.getVoteId());
	    response.put("timestamp", savedVote.getTimeStamp());

	    return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}




}
