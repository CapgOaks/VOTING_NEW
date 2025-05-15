package com.capgemini.security4.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capgemini.security4.entity.Elections;
import com.capgemini.security4.entity.Results;
import com.capgemini.security4.exception.ElectionNotFoundException;
import com.capgemini.security4.exception.ResultNotFoundException;
import com.capgemini.security4.repository.ElectionsRepository;
import com.capgemini.security4.repository.ResultsRepository;
import com.capgemini.security4.repository.VotesRepository;

@Service
public class ResultsServiceImpl implements ResultsService {

	private ResultsRepository resultsRepository;
	private VotesRepository votesRepository;
	private ElectionsRepository electionsRepository;

	@Autowired
	public ResultsServiceImpl(ResultsRepository resultsRepository, VotesRepository votesRepository,
			ElectionsRepository electionsRepository) {
		this.resultsRepository = resultsRepository;
		this.votesRepository = votesRepository;
		this.electionsRepository = electionsRepository;
	}

	@Override
	public Results createResult(Results result) {
		return resultsRepository.save(result);
	}

	@Override
	public Results updateResult(Long id, Results result) {
		Results dbResult = resultsRepository.findById(id).orElseThrow(() -> new ResultNotFoundException(id));

		dbResult.setCandidate(result.getCandidate());
		dbResult.setElection(result.getElection());
		dbResult.setTotalVotes(result.getTotalVotes());
		dbResult.setDeclaredAt(result.getDeclaredAt());

		return resultsRepository.save(dbResult);
	}

	@Override
	public void deleteResult(Long id) {
		Results result = resultsRepository.findById(id).orElseThrow(() -> new ResultNotFoundException(id));

		resultsRepository.delete(result);

	}

	@Override
	public Results findResult(Long id) {
		return resultsRepository.findById(id).orElseThrow(() -> new ResultNotFoundException(id));
	}

	@Override
	public List<Results> findAllResults() {
		return resultsRepository.findAll();
	}

	@Override
	public List<Results> findResultsByElectionId(Long electionId) {
		return resultsRepository.findAllByElectionId(electionId);
	}

	@Override
	public Results declareResult(Long electionId) {
		Elections election = electionsRepository.findById(electionId)
				.orElseThrow(() -> new ElectionNotFoundException("Election not found with id " + electionId));

		if (Boolean.FALSE.equals(election.getElectionStatus())) {
			throw new IllegalStateException("Results already declared for election " + electionId);
		}

		VotesRepository.WinnerProjection winner = votesRepository.findTopCandidateIdAndVotes(electionId);

		if (winner == null) {
			throw new IllegalStateException("No votes cast for election " + electionId);
		}

		Long candidateId = winner.getCandidateId();
		Long totalVotes = winner.getTotalVotes();

		Results result = new Results();
		result.setCandidateId(candidateId);
		result.setElectionId(electionId);
		result.setTotalVotes(totalVotes);
		result.setDeclaredAt(LocalDateTime.now());

		Results savedResult = resultsRepository.save(result);

		election.setElectionStatus(false);
		electionsRepository.save(election);

		return savedResult;
	}

}
