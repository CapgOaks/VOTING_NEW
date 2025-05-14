package com.capgemini.security4.service;

import java.util.List;

import com.capgemini.security4.entity.Results;

public interface ResultsService {
	Results createResult(Results results);

	Results updateResult(Long id, Results results);

	void deleteResult(Long id);

	Results findResult(Long id);

	List<Results> findAllResults();

	List<Results> findResultsByElectionId(Long electionId);

	Results declareResult(Long electionId);
}
