package com.capgemini.security4.service;

import java.util.List;

import com.capgemini.security4.dto.CandidateDto;
import com.capgemini.security4.entity.Candidates;

public interface CandidatesService {
	List<Candidates> getAllCandidates();

	Candidates getCandidatesById(Long candidateId);

	List<CandidateDto> getCandidatesByElectionId(Long electionId);

	Candidates createCandidates(Candidates candidates);

	Candidates updateCandidates(Long candidateId, Candidates candidates);

	void deleteCandidates(Long candidateId);
}
