package com.capgemini.security4.controller;



import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.service.VotesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
public class VotesController {

    private VotesService votesService;
    
    @Autowired
    public VotesController(VotesService votesService) {
        this.votesService = votesService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Boolean> checkUserVoteStatus(@PathVariable Long userId) {
        boolean hasVoted = votesService.hasUserVoted(userId);
        return ResponseEntity.ok(hasVoted);
    }

    @PostMapping
    public ResponseEntity<Votes> castVote(@RequestBody Votes vote) {
        Votes savedVote = votesService.castVote(vote);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedVote);
    }
}
