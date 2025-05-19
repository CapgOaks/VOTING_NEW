package com.capgemini.security4.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capgemini.security4.entity.Results;
import com.capgemini.security4.service.ResultsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/results")
@Slf4j
public class ResultsController {

	ResultsServiceImpl resultsService;

	public ResultsController(ResultsServiceImpl resultsService) {
		this.resultsService = resultsService;
	}

	@GetMapping({ "/", "" })
	public ResponseEntity<List<Results>> getAllResults() {
		log.info("GET /api/results - Fetching all election results");
		List<Results> results = resultsService.findAllResults();
		log.info("Fetched {} results", results.size());
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	@GetMapping("/{electionId}")
	public ResponseEntity<List<Results>> getResultsByElectionId(@PathVariable Long electionId) {
		log.info("GET /api/results/{} - Fetching results for electionId={}", electionId, electionId);
		List<Results> results = resultsService.findResultsByElectionId(electionId);
		log.info("Found {} result(s) for electionId={}", results.size(), electionId);
		return ResponseEntity.status(HttpStatus.OK).body(results);
	}

	@PostMapping("/declare/{electionId}")
	public ResponseEntity<Results> declareResult(@PathVariable Long electionId) {

		log.info("POST /api/results/declare/{} - Declaring result for election", electionId);

		Results declaredResult = resultsService.declareResult(electionId);

		log.info("Result successfully declared for election {}: {}", electionId, declaredResult);
		return ResponseEntity.status(HttpStatus.CREATED).body(declaredResult);
	}

}
