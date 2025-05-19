package com.capgemini.security4.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
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

import com.capgemini.security4.dto.VoteRequestDto;
import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.entity.Users;
import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.exception.UserNotFoundException;
import com.capgemini.security4.security.SecurityUtils;
import com.capgemini.security4.service.CandidatesService;
import com.capgemini.security4.service.CandidatesServiceImpl;
import com.capgemini.security4.service.ElectionsService;
import com.capgemini.security4.service.ElectionsServiceImpl;
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
	private CandidatesService candidatesService;
	private ElectionsService electionsService;

	@Autowired
	public VotesController(VotesService votesService, UserService userService,
			CandidatesServiceImpl candidatesServiceImpl, ElectionsServiceImpl electionsServiceImpl) {
		super();
		this.votesService = votesService;
		this.userService = userService;
		this.candidatesService = candidatesServiceImpl;
		this.electionsService = electionsServiceImpl;
	}

	@GetMapping("/user/{userId}")
	public ResponseEntity<Boolean> checkUserVoteStatus(@PathVariable Long userId) {
		log.info("Checking vote status for user with ID: {}", userId);
		 if (!userService.existsByUserId(userId)) {
		        throw new UserNotFoundException("User with ID " + userId + " not found");
		    }

		boolean hasVoted = votesService.hasUserVoted(userId);

		log.info("User with ID: {} has voted: {}", userId, hasVoted);
		return ResponseEntity.ok(hasVoted);
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> castVote(@Valid @RequestBody VoteRequestDto voteRequestDto,
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new IllegalArgumentException(bindingResult.getFieldErrors().toString());
		}

		// Get authenticated user
		String userName = SecurityUtils.getCurrentUser().getUsername();
		Users currentUser = userService.findByUserNameOrUserEmail(userName, userName);
		if (currentUser == null) {
			throw new IllegalStateException("Authenticated user not found.");
		}

		// Retrieve candidate and election entities
		Candidates candidate = candidatesService.getCandidateEntityById(voteRequestDto.getCandidateId());
		Elections election = electionsService.getElectionById(voteRequestDto.getElectionId());

		if (candidate == null || election == null) {
			throw new IllegalArgumentException("Invalid candidate or election ID.");
		}

		// Construct vote
		Votes vote = new Votes();
		vote.setUser(currentUser);
		vote.setCandidate(candidate);
		vote.setElection(election);
		vote.setTimeStamp(LocalDateTime.now());

		Votes savedVote = votesService.castVote(vote);

		// Build response
		Map<String, Object> response = new HashMap<>();
		response.put("message", "Vote successfully cast");
		response.put("voteId", savedVote.getVoteId());
		response.put("timestamp", savedVote.getTimeStamp());

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
	
	@GetMapping("/election/{electionId}")
	public ResponseEntity<List<Map<String, Object>>> getResultsByElection(@PathVariable Long electionId) {
	    log.info("Fetching vote results for election ID: {}", electionId);
	    List<Map<String, Object>> results = votesService.getElectionResults(electionId);
	    return ResponseEntity.ok(results);
	}


}
