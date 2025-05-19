package com.capgemini.security4.dto;

import jakarta.validation.constraints.NotNull;

public class VoteRequestDto {
	@NotNull(message = "Candidate is required")
	private Long candidateId;

	@NotNull(message = "Election is required")
	private Long electionId;

	public VoteRequestDto(@NotNull(message = "Candidate is required") Long candidateId,
			@NotNull(message = "Election is required") Long electionId) {
		super();
		this.candidateId = candidateId;
		this.electionId = electionId;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public Long getElectionId() {
		return electionId;
	}

	public void setElectionId(Long electionId) {
		this.electionId = electionId;
	}

}
