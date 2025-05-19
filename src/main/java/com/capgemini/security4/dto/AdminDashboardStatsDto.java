package com.capgemini.security4.dto;

public class AdminDashboardStatsDto {
	private Long totalCandidates;
	private Long registeredVoters;
	private Long ongoingElections;
	private Long votesToday;

	public AdminDashboardStatsDto() {
	}

	// Constructor
	public AdminDashboardStatsDto(Long totalCandidates, Long registeredVoters, Long ongoingElections, Long votesToday) {
		this.totalCandidates = totalCandidates;
		this.registeredVoters = registeredVoters;
		this.ongoingElections = ongoingElections;
		this.votesToday = votesToday;
	}

	public Long getTotalCandidates() {
		return totalCandidates;
	}

	public void setTotalCandidates(Long totalCandidates) {
		this.totalCandidates = totalCandidates;
	}

	public Long getRegisteredVoters() {
		return registeredVoters;
	}

	public void setRegisteredVoters(Long registeredVoters) {
		this.registeredVoters = registeredVoters;
	}

	public Long getOngoingElections() {
		return ongoingElections;
	}

	public void setOngoingElections(Long ongoingElections) {
		this.ongoingElections = ongoingElections;
	}

	public Long getVotesToday() {
		return votesToday;
	}

	public void setVotesToday(Long votesToday) {
		this.votesToday = votesToday;
	}

	@Override
	public String toString() {
		return "AdminDashboardStatsDto [totalCandidates=" + totalCandidates + ", registeredVoters=" + registeredVoters
				+ ", ongoingElections=" + ongoingElections + ", votesToday=" + votesToday + "]";
	}
	
	
}
