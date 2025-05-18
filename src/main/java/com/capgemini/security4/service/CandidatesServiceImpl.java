package com.capgemini.security4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.dto.CandidatesDto;
import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.exception.CandidateNotFound;
import com.capgemini.security4.exception.ElectionNotFoundException;
import com.capgemini.security4.exception.PartyNotFoundException;
import com.capgemini.security4.exception.UserNotFoundException;
import com.capgemini.security4.repository.CandidatesRepository;
import com.capgemini.security4.repository.ElectionsRepository;
import com.capgemini.security4.repository.PartyRepository;
import com.capgemini.security4.repository.UserRepository;

import jakarta.transaction.Transactional;
@Service
@Transactional
public class CandidatesServiceImpl implements CandidatesService {
	private final CandidatesRepository candidatesRepo;
    private final PartyRepository partyRepo;
    private final UserRepository userRepo;
    private final ElectionsRepository electionRepo;

    @Autowired
    public CandidatesServiceImpl(CandidatesRepository candidatesRepo,
                                PartyRepository partyRepo,
                                UserRepository userRepo,
                                ElectionsRepository electionRepo) {
        this.candidatesRepo = candidatesRepo;
        this.partyRepo = partyRepo;
        this.userRepo = userRepo;
        this.electionRepo = electionRepo;
    }
    @Override
    public List<CandidatesDto> getAllCandidates() {
        return candidatesRepo.findAll().stream()
            .map(this::convertToDto)
            .toList();
    }

    @Override
    public CandidatesDto getCandidatesById(Long id) {
        Candidates entity = candidatesRepo.findById(id)
            .orElseThrow(() -> new CandidateNotFound("Candidate not found with ID: " + id));
        return convertToDto(entity);
    }

    @Override
    public CandidatesDto createCandidates(CandidatesDto dto) {
    	userRepo.findById(dto.getUserId())
        .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + dto.getUserId()));
    
    electionRepo.findById(dto.getElectionId())
        .orElseThrow(() -> new ElectionNotFoundException("Election not found with ID: " + dto.getElectionId()));

    if(dto.getPartyId() != null) {
        partyRepo.findById(dto.getPartyId())
            .orElseThrow(() -> new PartyNotFoundException("Party not found with ID: " + dto.getPartyId()));
    }

    Candidates candidate = new Candidates();
    candidate.setUserId(dto.getUserId());
    candidate.setPartyId(dto.getPartyId());  // Can be null
    candidate.setElectionId(dto.getElectionId());
    candidate.setManifesto(dto.getManifesto());
        return convertToDto(candidatesRepo.save(candidate));
    }

    @Override
    public CandidatesDto updateCandidates(Long id, CandidatesDto dto) {
        Candidates existing = candidatesRepo.findById(id)
            .orElseThrow(() -> new CandidateNotFound("Candidate not found with ID: " + id));

        updateEntityFromDto(dto, existing);
        Candidates updated = candidatesRepo.save(existing);
        return convertToDto(updated);
    }

    @Override
    public void deleteCandidates(Long id) {
        if (!candidatesRepo.existsById(id)) {
            throw new CandidateNotFound("Candidate not found with ID: " + id);
        }
        candidatesRepo.deleteById(id);
    }

    // Conversion helpers
    private CandidatesDto convertToDto(Candidates entity) {
        return new CandidatesDto(
            entity.getCandidateId(),
            entity.getUserId(),
            entity.getPartyId(),
            entity.getElectionId(),
            entity.getManifesto()
        );
    }

    private Candidates convertToEntity(CandidatesDto dto) {
        return new Candidates(
            dto.getUserId(),
            dto.getPartyId(),
            dto.getElectionId(),
            dto.getManifesto()
        );
    }

    private void updateEntityFromDto(CandidatesDto dto, Candidates entity) {
        if (dto.getUserId() != null) entity.setUserId(dto.getUserId());
        if (dto.getPartyId() != null) entity.setPartyId(dto.getPartyId());
        if (dto.getElectionId() != null) entity.setElectionId(dto.getElectionId());
        if (dto.getManifesto() != null) entity.setManifesto(dto.getManifesto());
    }
}