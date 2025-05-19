package com.capgemini.security4.service;



import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.entity.Votes;
import com.capgemini.security4.exception.UserNotFoundException;
import com.capgemini.security4.repository.UserRepository;
import com.capgemini.security4.repository.VotesRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VotesServiceImpl implements VotesService {

	 private final UserRepository userRepository;
	private VotesRepository votesRepository;
    
    @Autowired
    public VotesServiceImpl(VotesRepository votesRepository, UserRepository userRepository) {
        this.votesRepository = votesRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean hasUserVoted(Long userId) {
    	if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
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

        vote.setTimeStamp(LocalDateTime.now());
        return votesRepository.save(vote);
    }
    
    @Override
    public List<Map<String, Object>> getElectionResults(Long electionId) {
        List<Object[]> result = votesRepository.countVotesByCandidateInElection(electionId);
        Long totalVotes = votesRepository.countTotalVotesInElection(electionId);

        List<Map<String, Object>> results = new ArrayList<>();
        for (Object[] row : result) {
            Candidates candidate = (Candidates) row[0];
            Long voteCount = (Long) row[1];
            double percentage = totalVotes == 0 ? 0 : (voteCount * 100.0) / totalVotes;

            Map<String, Object> map = new HashMap<>();
            map.put("userName", candidate.getUser().getUserName()); 
            map.put("partyName", candidate.getParty().getPartyName()); // if linked
            map.put("voteCount", voteCount);
            map.put("percentage", Math.round(percentage * 100.0) / 100.0);
            results.add(map);
        }
        return results;
    }
   
}
