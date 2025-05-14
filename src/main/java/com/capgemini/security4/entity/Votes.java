package com.capgemini.security4.entity;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Votes")
public class Votes {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long voteId;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false, referencedColumnName = "user_id", insertable = false, updatable = false)
	private Users user;

	@ManyToOne
	@JoinColumn(name = "candidate_id", nullable = false, referencedColumnName = "candidate_id", insertable = false, updatable = false)
	private Candidates candidate;

	@ManyToOne
	@JoinColumn(name = "election_id", nullable = false, referencedColumnName = "election_id", insertable = false, updatable = false)
	private Elections election;

	@Column(nullable = false)
	private LocalDateTime timeStamp;

	// Getters and Setters
	public Votes() {
		super();
	}

	public Votes(Long voteId, Users user, Candidates candidate, Elections election, LocalDateTime timeStamp) {
		super();
		this.voteId = voteId;
		this.user = user;
		this.candidate = candidate;
		this.election = election;
		this.timeStamp = timeStamp;
	}

	public Long getVoteId() {
		return voteId;
	}

	public void setVoteId(Long voteId) {
		this.voteId = voteId;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Candidates getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidates candidate) {
		this.candidate = candidate;
	}

	public Elections getElection() {
		return election;
	}

	public void setElection(Elections election) {
		this.election = election;
	}

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public void setTimestamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public String toString() {
		return "Votes [voteId=" + voteId + ", user=" + user + ", timestamp=" + timeStamp + "]";
	}

}
