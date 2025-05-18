package com.capgemini.security4.service;

import java.util.List;

import com.capgemini.security4.dto.RunningCandidateDto;
import com.capgemini.security4.entity.Candidates;

public interface CandidatesService {
	List<Candidates> getAllCandidates();
	Candidates getCandidatesById(Long candidateId);
	Candidates createCandidates(Candidates candidates);

	Candidates updateCandidates(Long candidateId, Candidates candidates);

	void deleteCandidates(Long candidateId);

	List<RunningCandidateDto> getRunningCandidates();
}
