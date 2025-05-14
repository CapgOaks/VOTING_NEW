package com.capgemini.security4.service;



import com.capgemini.security4.entity.Votes;


public interface VotesService {
    boolean hasUserVoted(Long userId);
    Votes castVote(Votes vote);
}
