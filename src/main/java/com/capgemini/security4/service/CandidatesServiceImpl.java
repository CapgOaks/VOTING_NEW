package com.capgemini.security4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.exception.CandidateNotFound;
import com.capgemini.security4.repository.CandidatesRepository;

@Service
public class CandidatesServiceImpl implements CandidatesService {
	private CandidatesRepository candidatesRepository;
	
	@Autowired
	public CandidatesServiceImpl(CandidatesRepository candidatesRepository) {
		super();
		this.candidatesRepository = candidatesRepository;
	}

	@Override
	public List<Candidates> getAllCandidates() {
		return candidatesRepository.findAll();
	}

	@Override
	public Candidates getCandidatesById(Long candidateId) {
		return candidatesRepository.findById(candidateId).orElseThrow(()->new CandidateNotFound("Candidate with id "+candidateId+" Not Found"));
	}

	@Override
	public Candidates createCandidates(Candidates candidates) {
		return candidatesRepository.save(candidates);
	}

	@Override
	public Candidates updateCandidates(Long candidateId, Candidates candidates) {
		Candidates existing=candidatesRepository.findById(candidateId).orElseThrow(()->new CandidateNotFound("Candidate with id "+candidateId+" Not Found"));
		if(candidates.getParty()!=null) {
			existing.setParty(candidates.getParty());
		}
		if (candidates.getUser() != null) {
            existing.setUser(candidates.getUser());
        }
        if (candidates.getManifesto() != null) {
            existing.setManifesto(candidates.getManifesto());
        }
        if (candidates.getElection() != null) {
            existing.setElection(candidates.getElection());
        }
		return candidatesRepository.save(existing);
	}

	@Override
	public void deleteCandidates(Long candidateId) {
		Candidates existing=candidatesRepository.findById(candidateId).orElseThrow(()->new CandidateNotFound("Candidate with id "+candidateId+" Not Found"));
		candidatesRepository.deleteById(candidateId);
		
	}

}
