package com.capgemini.security4.service;

//package com.capgemini.security4.service.impl;

import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.repository.VotesRepository;
import com.capgemini.security4.service.VotesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
public class VotesServiceImpl implements VotesService {

    private VotesRepository votesRepository;
    
    @Autowired
    public VotesServiceImpl(VotesRepository votesRepository) {
        this.votesRepository = votesRepository;
    }

    @Override
    public boolean hasUserVoted(Long userId) {
        return votesRepository.existsByUser_UserId(userId);
    }

    @Override
    public Votes castVote(Votes vote) {
        if (vote.getUser() == null || vote.getCandidate() == null || vote.getElection() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User, candidate, and election must be specified");
        }

        if (hasUserVoted(vote.getUser().getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User has already voted");
        }

        vote.setTimestamp(LocalDateTime.now());
        return votesRepository.save(vote);
    }
}
