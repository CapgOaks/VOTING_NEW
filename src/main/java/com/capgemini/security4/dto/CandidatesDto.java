package com.capgemini.security4.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CandidatesDto {
	private Long candidateId;

	@NotNull(message = "User ID is required")
	private Long userId;

	@NotNull(message = "Party ID is required")
	private Long partyId;

	@NotNull(message = "Election ID is required")
	private Long electionId;

	@Size(max = 1000, message = "Manifesto must be ≤1000 characters")
	private String manifesto;
}
