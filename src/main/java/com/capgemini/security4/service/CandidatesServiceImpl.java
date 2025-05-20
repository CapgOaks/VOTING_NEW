package com.capgemini.security4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.dto.CandidateDto;
import com.capgemini.security4.dto.CandidatesDto;
import com.capgemini.security4.dto.RunningCandidateDto;
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

    private static final String CANDIDATE_NOT_FOUND = "Candidate not found with ID: ";
    private static final String USER_NOT_FOUND = "User not found with ID: ";
    private static final String ELECTION_NOT_FOUND = "Election not found with ID: ";
    private static final String PARTY_NOT_FOUND = "Party not found with ID: ";

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
                .orElseThrow(() -> new CandidateNotFound(CANDIDATE_NOT_FOUND + id));
        return convertToDto(entity);
    }

    @Override
    public Candidates getCandidateEntityById(Long id) {
        return candidatesRepo.findById(id)
                .orElseThrow(() -> new CandidateNotFound(CANDIDATE_NOT_FOUND + id));
    }

    @Override
    public CandidatesDto createCandidates(CandidatesDto dto) {
        // Validate associated user
        userRepo.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND + dto.getUserId()));

        // Validate associated election
        electionRepo.findById(dto.getElectionId())
                .orElseThrow(() -> new ElectionNotFoundException(ELECTION_NOT_FOUND + dto.getElectionId()));

        // Validate associated party if provided
        if (dto.getPartyId() != null) {
            partyRepo.findById(dto.getPartyId())
                    .orElseThrow(() -> new PartyNotFoundException(PARTY_NOT_FOUND + dto.getPartyId()));
        }

        // Create and save new candidate entity
        Candidates candidate = new Candidates();
        candidate.setUserId(dto.getUserId());
        candidate.setPartyId(dto.getPartyId());
        candidate.setElectionId(dto.getElectionId());
        candidate.setManifesto(dto.getManifesto());

        return convertToDto(candidatesRepo.save(candidate));
    }

    @Override
    public CandidatesDto updateCandidates(Long id, CandidatesDto dto) {
        Candidates existing = candidatesRepo.findById(id)
                .orElseThrow(() -> new CandidateNotFound(CANDIDATE_NOT_FOUND + id));

        updateEntityFromDto(dto, existing);
        Candidates updated = candidatesRepo.save(existing);

        return convertToDto(updated);
    }

    @Override
    public void deleteCandidates(Long id) {
        if (!candidatesRepo.existsById(id)) {
            throw new CandidateNotFound(CANDIDATE_NOT_FOUND + id);
        }
        candidatesRepo.deleteById(id);
    }

    @Override
    public List<CandidateDto> getCandidatesByElectionId(Long electionId) {
        return candidatesRepo.findCandidateDtosByElectionId(electionId);
    }

    @Override
    public List<RunningCandidateDto> getRunningCandidates() {
        return candidatesRepo.findRunningCandidates("active");
    }

    // Helper method: Convert entity to DTO
    private CandidatesDto convertToDto(Candidates entity) {
        return new CandidatesDto(
                entity.getCandidateId(),
                entity.getUserId(),
                entity.getPartyId(),
                entity.getElectionId(),
                entity.getManifesto()
        );
    }

    // Helper method: Update an existing entity using DTO values
    private void updateEntityFromDto(CandidatesDto dto, Candidates entity) {
        if (dto.getUserId() != null) entity.setUserId(dto.getUserId());
        if (dto.getPartyId() != null) entity.setPartyId(dto.getPartyId());
        if (dto.getElectionId() != null) entity.setElectionId(dto.getElectionId());
        if (dto.getManifesto() != null) entity.setManifesto(dto.getManifesto());
    }
}
