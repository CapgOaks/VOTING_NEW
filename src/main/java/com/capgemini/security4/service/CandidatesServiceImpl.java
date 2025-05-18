package com.capgemini.security4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.dto.CandidateDto;
import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.exception.CandidateNotFound;
import com.capgemini.security4.repository.CandidatesRepository;

@Service
public class CandidatesServiceImpl implements CandidatesService {

    private static final String CANDIDATE_ID_PREFIX = "Candidate with id ";
    private static final String CANDIDATE_NOT_FOUND = " Not Found";

    private final CandidatesRepository candidatesRepository;

    @Autowired
    public CandidatesServiceImpl(CandidatesRepository candidatesRepository) {
        this.candidatesRepository = candidatesRepository;
    }

    @Override
    public List<Candidates> getAllCandidates() {
        return candidatesRepository.findAll();
    }

    @Override
    public Candidates getCandidatesById(Long candidateId) {
        return candidatesRepository.findById(candidateId)
                .orElseThrow(() -> new CandidateNotFound(CANDIDATE_ID_PREFIX + candidateId + CANDIDATE_NOT_FOUND));
    }
    
    public List<CandidateDto> getCandidatesByElectionId(Long electionId) {
        return candidatesRepository.findCandidateDtosByElectionId(electionId);
    }
    
    @Override
    public Candidates createCandidates(Candidates candidates) {
        return candidatesRepository.save(candidates);
    }

    @Override
    public Candidates updateCandidates(Long candidateId, Candidates candidates) {
        Candidates existing = candidatesRepository.findById(candidateId)
                .orElseThrow(() -> new CandidateNotFound(CANDIDATE_ID_PREFIX + candidateId + CANDIDATE_NOT_FOUND));

        if (candidates.getParty() != null) {
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
        candidatesRepository.findById(candidateId)
                .orElseThrow(() -> new CandidateNotFound(CANDIDATE_ID_PREFIX + candidateId + CANDIDATE_NOT_FOUND));
        candidatesRepository.deleteById(candidateId);
    }
}
