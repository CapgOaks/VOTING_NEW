package com.capgemini.security4.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capgemini.security4.entity.Results;
import com.capgemini.security4.service.ResultsServiceImpl;

@RestController
@RequestMapping("/api/results")
public class ResultsController {

	ResultsServiceImpl resultsService;

	public ResultsController(ResultsServiceImpl resultsService) {
		this.resultsService = resultsService;
	}

	@GetMapping({ "/", "" })
	public ResponseEntity<List<Results>> getAllResults() {
		return ResponseEntity.status(HttpStatus.OK).body(resultsService.findAllResults());
	}

	@GetMapping("/{electionId}")
	public ResponseEntity<List<Results>> getResultsByElectionId(@PathVariable Long electionId) {
		return ResponseEntity.status(HttpStatus.OK).body(resultsService.findResultsByElectionId(electionId));
	}

	@PostMapping("/decalre/{electionId}")
	public ResponseEntity<Results> declareResult(@PathVariable Long electionId) {
		return ResponseEntity.status(HttpStatus.CREATED).body(resultsService.declareResult(electionId));
	}
}
