package com.capgemini.security4.service;



import java.util.List;
import java.util.Map;

import com.capgemini.security4.entity.Votes;


public interface VotesService {
    boolean hasUserVoted(Long userId);
    Votes castVote(Votes vote);
    
    List<Map<String, Object>> getElectionResults(Long electionId);
    

}
