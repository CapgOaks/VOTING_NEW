package com.capgemini.security4.service;




import java.util.List;

import com.capgemini.security4.dto.CandidateDto;
import com.capgemini.security4.dto.CandidatesDto;
import com.capgemini.security4.dto.RunningCandidateDto;
import com.capgemini.security4.entity.Candidates;

public interface CandidatesService {
	List<CandidatesDto> getAllCandidates();
	CandidatesDto getCandidatesById(Long candidateId);
	Candidates getCandidateEntityById(Long id);
	CandidatesDto createCandidates(CandidatesDto candidatesDto);

	CandidatesDto updateCandidates(Long candidateId, CandidatesDto candidatesDto);

	List<CandidateDto> getCandidatesByElectionId(Long electionId);

	void deleteCandidates(Long candidateId);

	List<RunningCandidateDto> getRunningCandidates();
}
