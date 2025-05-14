package com.capgemini.security4.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.capgemini.security4.entity.Candidates;
import com.capgemini.security4.entity.Results;
import com.capgemini.security4.exception.ResultNotFoundException;
import com.capgemini.security4.repository.ResultsRepository;

public class ResultsServiceImpl implements ResultsService {

	private ResultsRepository resultRepository;

	@Autowired
	public ResultsServiceImpl(ResultsRepository resultsRepository) {
		this.resultRepository = resultsRepository;
	}

	@Override
	public Results createResult(Results result) {
		return resultRepository.save(result);
	}

	@Override
	public Results updateResult(Long id, Results result) {
		Results dbResult = resultRepository.findById(id)
				.orElseThrow(() -> new ResultNotFoundException("Result with ID:" + id + " not found!"));

		dbResult.setCandidate(result.getCandidate());
		dbResult.setElection(result.getElection());
		dbResult.setTotalVotes(result.getTotalVotes());
		dbResult.setDeclaredAt(result.getDeclaredAt());

		return resultRepository.save(dbResult);
	}

	@Override
	public void deleteResult(Long id) {
		Results result = resultRepository.findById(id)
				.orElseThrow(() -> new ResultNotFoundException("Result with ID:" + id + " not found!"));

		resultRepository.delete(result);

	}

	@Override
	public Results findResult(Long id) {
		return resultRepository.findById(id)
				.orElseThrow(() -> new ResultNotFoundException("Result with ID:" + id + " not found!"));
	}

	@Override
	public List<Results> findAllResults() {
		return resultRepository.findAll();
	}

	@Override
	public List<Results> findResultsByElectionId(Long electionId) {
		return resultRepository.findAllByElectionId(electionId);
	}

	@Override
	public Results declareResult(Long electionId) {
		return null;
	}

}
