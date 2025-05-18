package com.capgemini.security4.service;

import java.util.List;

import com.capgemini.security4.dto.CandidatesDto;
import com.capgemini.security4.entity.Candidates;

public interface CandidatesService {
	List<CandidatesDto> getAllCandidates();
	CandidatesDto getCandidatesById(Long candidateId);
	CandidatesDto createCandidates(CandidatesDto candidatesDto);

	CandidatesDto updateCandidates(Long candidateId, CandidatesDto candidatesDto);

	void deleteCandidates(Long candidateId);
}
